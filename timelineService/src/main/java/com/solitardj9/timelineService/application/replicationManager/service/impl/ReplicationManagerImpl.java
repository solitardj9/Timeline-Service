package com.solitardj9.timelineService.application.replicationManager.service.impl;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solitardj9.timelineService.application.messageFlowManager.model.ConsumeAck;
import com.solitardj9.timelineService.application.messageFlowManager.model.ConsumeMessage;
import com.solitardj9.timelineService.application.messageFlowManager.model.PublishAck;
import com.solitardj9.timelineService.application.messageFlowManager.model.PublishMessage;
import com.solitardj9.timelineService.application.replicationManager.model.ReplicationParamEnum.ReplicationTypeParamEnum;
import com.solitardj9.timelineService.application.replicationManager.service.ReplicationManager;
import com.solitardj9.timelineService.application.timelineManager.model.PutValue;
import com.solitardj9.timelineService.application.timelineManager.model.PutValues;
import com.solitardj9.timelineService.application.timelineManager.model.Remove;
import com.solitardj9.timelineService.application.timelineManager.model.RemoveByBefore;
import com.solitardj9.timelineService.application.timelineManager.model.RemoveByPeriod;
import com.solitardj9.timelineService.application.timelineManager.model.RemoveByTimes;
import com.solitardj9.timelineService.application.timelineManager.service.TimelineManager;
import com.solitardj9.timelineService.application.timelineManager.service.exception.ExceptionTimelineConflictFailure;
import com.solitardj9.timelineService.application.timelineManager.service.exception.ExceptionTimelineInternalFailure;
import com.solitardj9.timelineService.application.timelineManager.service.exception.ExceptionTimelineResourceNotFound;
import com.solitardj9.timelineService.application.timelineSyncManager.service.TimelineRestoreManager;
import com.solitardj9.timelineService.service.serviceInstancesManager.model.ServiceInstanceParamEnum.ServiceInstanceClusterStatus;
import com.solitardj9.timelineService.service.serviceInstancesManager.service.ServiceInstancesManager;

@Service("replicationManager")
public class ReplicationManagerImpl implements ReplicationManager {
	//
	private static final Logger logger = LoggerFactory.getLogger(ReplicationManagerImpl.class);
	
	@Autowired
	ApplicationEventPublisher applicationEventPublisher;
	
	@Autowired
	TimelineManager timelineManager;
	
	@Autowired
	TimelineRestoreManager timelineRestoreManager;
	
	@Autowired
	ServiceInstancesManager serviceInstancesManager;
	
	private ObjectMapper om = new ObjectMapper();
	
	private Boolean isServiceAvailable() {
		//
		if (serviceInstancesManager.isClustered().equals(ServiceInstanceClusterStatus.RESTORED)
				|| serviceInstancesManager.isClustered().equals(ServiceInstanceClusterStatus.ONLINE)) {
			return true;
		}
		return false;
	}
	
	@EventListener(classes=ConsumeMessage.class, condition = "#consumeMessage.type == 'addTimeline'")
	public void onConsumeMessageForReplicate(ConsumeMessage consumeMessage) {
		//
		String timeline = consumeMessage.getMessage();
		try {
			timelineManager.addTimeline(timeline);
		} catch (ExceptionTimelineConflictFailure e) {
			logger.error("[ReplicationManager].onConsumeMessageForReplicate : error = " + e);
		} finally {
			publishConsumeAck(consumeMessage.getClientId(), consumeMessage.getConsumerTag());
		}
	}
	
	@EventListener(classes=ConsumeMessage.class, condition = "#consumeMessage.type == 'deleteTimeline'")
	public void onConsumeMessageForDelete(ConsumeMessage consumeMessage) {
		//
		if (isServiceAvailable()) {
			String timeline = consumeMessage.getMessage();
			try {
				timelineManager.deleteTimeline(timeline);
			} catch (ExceptionTimelineResourceNotFound e) {
				logger.error("[ReplicationManager].onConsumeMessageForDelete : error = " + e);
			} finally {
				publishConsumeAck(consumeMessage.getClientId(), consumeMessage.getConsumerTag());
			}
		}
		else {
			timelineRestoreManager.addReplicationEvent(consumeMessage);
			
			publishConsumeAck(consumeMessage.getClientId(), consumeMessage.getConsumerTag());
		}
	}
	
	@EventListener(classes=ConsumeMessage.class, condition = "#consumeMessage.type == 'put'")
	public void onConsumeMessageForPut(ConsumeMessage consumeMessage) {
		//
		String strPutValue = consumeMessage.getMessage();
		try {
			PutValue putValue = om.readValue(strPutValue, PutValue.class);
			timelineManager.put(putValue.getTimeline(), putValue.getTimestamp(), putValue.getValue());
		} catch (ExceptionTimelineResourceNotFound | ExceptionTimelineInternalFailure | JsonProcessingException e) {
			logger.error("[ReplicationManager].onConsumeMessageForPut : error = " + e);
		} finally {
			publishConsumeAck(consumeMessage.getClientId(), consumeMessage.getConsumerTag());
		}
	}
	
	@EventListener(classes=ConsumeMessage.class, condition = "#consumeMessage.type == 'putAll'")
	public void onConsumeMessageForPutAll(ConsumeMessage consumeMessage) {
		//
		String strPutValues = consumeMessage.getMessage();
		try {
			PutValues putValues = om.readValue(strPutValues, PutValues.class);
			timelineManager.putAll(putValues.getTimeline(), putValues.getValues());
		} catch (ExceptionTimelineResourceNotFound | ExceptionTimelineInternalFailure | JsonProcessingException e) {
			logger.error("[ReplicationManager].onConsumeMessageForPutAll : error = " + e);
		} finally {
			publishConsumeAck(consumeMessage.getClientId(), consumeMessage.getConsumerTag());
		}
	}
	
	@EventListener(classes=ConsumeMessage.class, condition = "#consumeMessage.type == 'update'")
	public void onConsumeMessageForUpdate(ConsumeMessage consumeMessage) {
		//
		String strPutValue = consumeMessage.getMessage();
		try {
			PutValue putValue = om.readValue(strPutValue, PutValue.class);
			timelineManager.put(putValue.getTimeline(), putValue.getTimestamp(), putValue.getValue());
		} catch (ExceptionTimelineResourceNotFound | ExceptionTimelineInternalFailure | JsonProcessingException e) {
			logger.error("[ReplicationManager].onConsumeMessageForUpdate : error = " + e);
		} finally {
			publishConsumeAck(consumeMessage.getClientId(), consumeMessage.getConsumerTag());
		}
	}
	
	@EventListener(classes=ConsumeMessage.class, condition = "#consumeMessage.type == 'updateAll'")
	public void onConsumeMessageForUpdateAll(ConsumeMessage consumeMessage) {
		//
		String strPutValues = consumeMessage.getMessage();
		try {
			PutValues putValues = om.readValue(strPutValues, PutValues.class);
			timelineManager.putAll(putValues.getTimeline(), putValues.getValues());
		} catch (ExceptionTimelineResourceNotFound | ExceptionTimelineInternalFailure | JsonProcessingException e) {
			logger.error("[ReplicationManager].onConsumeMessageForUpdateAll : error = " + e);
		} finally {
			publishConsumeAck(consumeMessage.getClientId(), consumeMessage.getConsumerTag());
		}
	}
	
	@EventListener(classes=ConsumeMessage.class, condition = "#consumeMessage.type == 'remove'")
	public void onConsumeMessageForRemove(ConsumeMessage consumeMessage) {
		//
		if (isServiceAvailable()) {
			String strRemove = consumeMessage.getMessage();
			try {
				Remove remove = om.readValue(strRemove, Remove.class);
				timelineManager.remove(remove.getTimeline(), remove.getTimestamp());
			} catch (ExceptionTimelineResourceNotFound | ExceptionTimelineInternalFailure | JsonProcessingException e) {
				logger.error("[ReplicationManager].onConsumeMessageForRemove : error = " + e);
			} finally {
				publishConsumeAck(consumeMessage.getClientId(), consumeMessage.getConsumerTag());
			}
		}
		else {
			timelineRestoreManager.addReplicationEvent(consumeMessage);
			
			publishConsumeAck(consumeMessage.getClientId(), consumeMessage.getConsumerTag());
		}
	}
	
	@EventListener(classes=ConsumeMessage.class, condition = "#consumeMessage.type == 'removeByTimes'")
	public void onConsumeMessageForRemoveByTimes(ConsumeMessage consumeMessage) {
		//
		if (isServiceAvailable()) {
			String strRemoveByTimes = consumeMessage.getMessage();
			try {
				RemoveByTimes removeByTimes = om.readValue(strRemoveByTimes, RemoveByTimes.class);
				timelineManager.removeByTimes(removeByTimes.getTimeline(), removeByTimes.getTimestamps());
			} catch (ExceptionTimelineResourceNotFound | ExceptionTimelineInternalFailure | JsonProcessingException e) {
				logger.error("[ReplicationManager].onConsumeMessageForRemoveByTimes : error = " + e);
			} finally {
				publishConsumeAck(consumeMessage.getClientId(), consumeMessage.getConsumerTag());
			}
		}
		else {
			timelineRestoreManager.addReplicationEvent(consumeMessage);
			
			publishConsumeAck(consumeMessage.getClientId(), consumeMessage.getConsumerTag());
		}
	}
	
	@EventListener(classes=ConsumeMessage.class, condition = "#consumeMessage.type == 'removeByPeriod'")
	public void onConsumeMessageForRemoveByPeriod(ConsumeMessage consumeMessage) {
		//
		if (isServiceAvailable()) {
			String strRemoveByPeriod = consumeMessage.getMessage();
			try {
				RemoveByPeriod removeByPeriod = om.readValue(strRemoveByPeriod, RemoveByPeriod.class);
				timelineManager.removeByPeriod(removeByPeriod.getTimeline(), removeByPeriod.getFromTimestamp(), removeByPeriod.getToTimestamp());
			} catch (ExceptionTimelineResourceNotFound | JsonProcessingException e) {
				logger.error("[ReplicationManager].onConsumeMessageForRemoveByPeriod : error = " + e);
			} finally {
				publishConsumeAck(consumeMessage.getClientId(), consumeMessage.getConsumerTag());
			}
		}
		else {
			timelineRestoreManager.addReplicationEvent(consumeMessage);
			
			publishConsumeAck(consumeMessage.getClientId(), consumeMessage.getConsumerTag());
		}
	}
	
	@EventListener(classes=ConsumeMessage.class, condition = "#consumeMessage.type == 'removeByBefore'")
	public void onConsumeMessageForRemoveByBefore(ConsumeMessage consumeMessage) {
		//
		if (isServiceAvailable()) {
			String strRemoveByBefore = consumeMessage.getMessage();
			try {
				RemoveByBefore removeByBefore = om.readValue(strRemoveByBefore, RemoveByBefore.class);
				timelineManager.removeByBefore(removeByBefore.getTimeline(), removeByBefore.getToTimestamp());
			} catch (ExceptionTimelineResourceNotFound | JsonProcessingException e) {
				logger.error("[ReplicationManager].onConsumeMessageForRemoveByBefore : error = " + e);
			} finally {
				publishConsumeAck(consumeMessage.getClientId(), consumeMessage.getConsumerTag());
			}
		}
		else {
			timelineRestoreManager.addReplicationEvent(consumeMessage);
			
			publishConsumeAck(consumeMessage.getClientId(), consumeMessage.getConsumerTag());
		}
	}
	
	@EventListener(classes=ConsumeMessage.class, condition = "#consumeMessage.type == 'clear'")
	public void onConsumeMessageForClear(ConsumeMessage consumeMessage) {
		//
		if (isServiceAvailable()) {
			String timeline = consumeMessage.getMessage();
			try {
				timelineManager.clear(timeline);
			} catch (ExceptionTimelineResourceNotFound | ExceptionTimelineInternalFailure  e) {
				logger.error("[ReplicationManager].onConsumeMessageForClear : error = " + e);
			} finally {
				publishConsumeAck(consumeMessage.getClientId(), consumeMessage.getConsumerTag());
			}
		}
		else {
			timelineRestoreManager.addReplicationEvent(consumeMessage);
			
			publishConsumeAck(consumeMessage.getClientId(), consumeMessage.getConsumerTag());
		}
	}

	@Override
	public void replicateAddTimeline(String timeline) {
		//
		publishReplicationMessage(ReplicationTypeParamEnum.ADD_TIMELINE.getType(), timeline);
	}

	@Override
	public void replicateDeleteTimeline(String timeline) {
		//
		publishReplicationMessage(ReplicationTypeParamEnum.DELETE_TIMELINE.getType(), timeline);
	}

	@Override
	public void replicatePut(PutValue putValue) {
		//
		try {
			String message = om.writeValueAsString(putValue);
			publishReplicationMessage(ReplicationTypeParamEnum.PUT.getType(), message);
		} catch (JsonProcessingException e) {
			logger.error("[ReplicationManager].replicatePut : error = " + e);
		}
	}

	@Override
	public void replicatePutAll(PutValues putValues) {
		//
		try {
			String message = om.writeValueAsString(putValues);
			publishReplicationMessage(ReplicationTypeParamEnum.PUT_ALL.getType(), message);
		} catch (JsonProcessingException e) {
			logger.error("[ReplicationManager].replicatePutAll : error = " + e);
		}
	}

	@Override
	public void replicateUpdate(PutValue putValue) {
		//
		try {
			String message = om.writeValueAsString(putValue);
			publishReplicationMessage(ReplicationTypeParamEnum.UPDATE.getType(), message);
		} catch (JsonProcessingException e) {
			logger.error("[ReplicationManager].replicateUpdate : error = " + e);
		}
	}

	@Override
	public void replicateUpdateAll(PutValues putValues) {
		//
		try {
			String message = om.writeValueAsString(putValues);
			publishReplicationMessage(ReplicationTypeParamEnum.UPDATE_ALL.getType(), message);
		} catch (JsonProcessingException e) {
			logger.error("[ReplicationManager].replicateUpdateAll : error = " + e);
		}
	}

	@Override
	public void replicateRemove(Remove remove) {
		//
		try {
			String message = om.writeValueAsString(remove);
			publishReplicationMessage(ReplicationTypeParamEnum.REMOVE.getType(), message);
		} catch (JsonProcessingException e) {
			logger.error("[ReplicationManager].replicateRemove : error = " + e);
		}
	}

	@Override
	public void replicateRemoveByTimes(RemoveByTimes removeByTimes) {
		//
		try {
			String message = om.writeValueAsString(removeByTimes);
			publishReplicationMessage(ReplicationTypeParamEnum.REMOVE_BY_TIMES.getType(), message);
		} catch (JsonProcessingException e) {
			logger.error("[ReplicationManager].replicateRemoveByTimes : error = " + e);
		}
	}

	@Override
	public void replicateRemoveByPeriod(RemoveByPeriod removeByPeriod) {
		//
		try {
			String message = om.writeValueAsString(removeByPeriod);
			publishReplicationMessage(ReplicationTypeParamEnum.REMOVE_BY_PERIOD.getType(), message);
		} catch (JsonProcessingException e) {
			logger.error("[ReplicationManager].replicateRemoveByPeriod : error = " + e);
		}
	}

	@Override
	public void replicateRemoveByBefore(RemoveByBefore removeByBefore) {
		//
		try {
			String message = om.writeValueAsString(removeByBefore);
			publishReplicationMessage(ReplicationTypeParamEnum.REMOVE_BY_BEFORE.getType(), message);
		} catch (JsonProcessingException e) {
			logger.error("[ReplicationManager].replicateRemoveByBefore : error = " + e);
		}
	}

	@Override
	public void replicateClear(String timeline) {
		//
		publishReplicationMessage(ReplicationTypeParamEnum.CLEAR.getType(), timeline);
	}
	
	private void publishReplicationMessage(String type, String message) {
		//
		PublishMessage publishMessage = new PublishMessage(UUID.randomUUID().toString(), type, message);
		applicationEventPublisher.publishEvent(publishMessage);
	}
	
	@EventListener
	@Async
	public void onReplicationMessageAckEvent(PublishAck publishAck) {
		//
		try {
			logger.info("[ReplicationManager].onReplicationMessageAckEvent : trace ID = " + publishAck.toString());
		} catch (Exception e) {
			logger.error("[ReplicationManager].onReplicationMessageAckEvent : " + e);
		}
	}
	
	private void publishConsumeAck(String clientId, String consumerTag) {
		//
		ConsumeAck consumeAck = new ConsumeAck(clientId, consumerTag);
		applicationEventPublisher.publishEvent(consumeAck);
	}
}