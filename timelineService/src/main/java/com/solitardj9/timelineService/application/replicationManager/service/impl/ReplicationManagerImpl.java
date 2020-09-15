package com.solitardj9.timelineService.application.replicationManager.service.impl;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import com.solitardj9.timelineService.application.messageFlowManager.model.ConsumeMessage;
import com.solitardj9.timelineService.application.replicationManager.service.ReplicationManager;
import com.solitardj9.timelineService.application.timelineManager.model.PutValue;
import com.solitardj9.timelineService.application.timelineManager.model.PutValues;
import com.solitardj9.timelineService.application.timelineManager.model.Remove;
import com.solitardj9.timelineService.application.timelineManager.model.RemoveByBefore;
import com.solitardj9.timelineService.application.timelineManager.model.RemoveByPeriod;
import com.solitardj9.timelineService.application.timelineManager.model.RemoveByTimes;

@Service("replicationManager")
public class ReplicationManagerImpl implements ReplicationManager {
	
	@EventListener(classes=ConsumeMessage.class, condition = "#consumeMessage.type == 'addTimeline'")
	public void onConsumeMessageForReplicate(ConsumeMessage consumeMessage) {
		
	}
	
	@EventListener(classes=ConsumeMessage.class, condition = "#consumeMessage.type == 'deleteTimeline'")
	public void onConsumeMessageForDelete(ConsumeMessage consumeMessage) {
		
	}
	
	@EventListener(classes=ConsumeMessage.class, condition = "#consumeMessage.type == 'put'")
	public void onConsumeMessageForPut(ConsumeMessage consumeMessage) {
		
	}
	
	@EventListener(classes=ConsumeMessage.class, condition = "#consumeMessage.type == 'putAll'")
	public void onConsumeMessageForPutAll(ConsumeMessage consumeMessage) {
		
	}
	
	@EventListener(classes=ConsumeMessage.class, condition = "#consumeMessage.type == 'update'")
	public void onConsumeMessageForUpdate(ConsumeMessage consumeMessage) {
		
	}
	
	@EventListener(classes=ConsumeMessage.class, condition = "#consumeMessage.type == 'updateAll'")
	public void onConsumeMessageForUpdateAll(ConsumeMessage consumeMessage) {
		
	}
	
	@EventListener(classes=ConsumeMessage.class, condition = "#consumeMessage.type == 'remove'")
	public void onConsumeMessageForRemove(ConsumeMessage consumeMessage) {
		
	}
	
	@EventListener(classes=ConsumeMessage.class, condition = "#consumeMessage.type == 'removeByTimes'")
	public void onConsumeMessageForRemoveByTimes(ConsumeMessage consumeMessage) {
		
	}
	
	@EventListener(classes=ConsumeMessage.class, condition = "#consumeMessage.type == 'removeByPeriod'")
	public void onConsumeMessageForRemoveByPeriod(ConsumeMessage consumeMessage) {
		
	}
	
	@EventListener(classes=ConsumeMessage.class, condition = "#consumeMessage.type == 'removeByBefore'")
	public void onConsumeMessageForRemoveByBefore(ConsumeMessage consumeMessage) {
		
	}
	
	@EventListener(classes=ConsumeMessage.class, condition = "#consumeMessage.type == 'clear'")
	public void onConsumeMessageForClear(ConsumeMessage consumeMessage) {
		
	}

	@Override
	public void replicateAddTimeline(String timeline) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void replicateDeleteTimeline(String timeline) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void replicatePut(PutValue putValue) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void replicatePutAll(PutValues putValues) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void replicateUpdate(PutValue putValue) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void replicateUpdateAll(PutValues putValues) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void replicateRemove(Remove remove) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void replicateRemoveByTimes(RemoveByTimes removeByTimes) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void replicateRemoveByPeriod(RemoveByPeriod removeByPeriod) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void replicateRemoveByBefore(RemoveByBefore removeByBefore) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void replicateClear(String timeline) {
		// TODO Auto-generated method stub
		
	}
}