package com.solitardj9.timelineService.application.timelineSyncManager.service.impl;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import com.solitardj9.timelineService.application.timelineSyncManager.service.TimelineSyncManager;
import com.solitardj9.timelineService.application.timelineSyncManager.service.data.TimelineMetadata;
import com.solitardj9.timelineService.application.timelineSyncManager.service.data.TimelineSyncManagerParamsEnum.TimelineClusterInfo;
import com.solitardj9.timelineService.application.timelineSyncManager.service.data.TimelineSyncManagerParamsEnum.TimelineExistStatus;
import com.solitardj9.timelineService.service.serviceInstancesManager.service.ServiceInstancesManager;
import com.solitardj9.timelineService.service.serviceInstancesManager.service.data.ServiceInstance;
import com.solitardj9.timelineService.systemInterface.httpInterface.service.HttpProxyAdaptor;
import com.solitardj9.timelineService.systemInterface.inMemoryInterface.model.InMemoryInstance;
import com.solitardj9.timelineService.systemInterface.inMemoryInterface.service.InMemoryManager;
import com.solitardj9.timelineService.systemInterface.inMemoryInterface.service.impl.exception.ExceptionHazelcastDataStructureCreationFailure;
import com.solitardj9.timelineService.systemInterface.inMemoryInterface.service.impl.exception.ExceptionHazelcastDataStructureNotFoundFailure;

@Service("timelineSyncManager")
public class TimelineSyncManagerImpl implements TimelineSyncManager {
	
	private static final Logger logger = LoggerFactory.getLogger(TimelineSyncManagerImpl.class);
	
	@Autowired
	TimelineManager timelineManager;
	
	@Autowired
	ReplicationManager replicationManager;
	
	@Autowired
	InMemoryManager inMemoryManager;
	
	@Autowired
	ServiceInstancesManager serviceInstancesManager;
	
	@Autowired
	HttpProxyAdaptor httpProxyAdaptor;
	
	private Integer backupCount = 2;
	
	private Boolean readBackupData = true;
	
	private ObjectMapper om = new ObjectMapper();
	
	@PostConstruct
	public void init() {
		//
		InMemoryInstance inMemoryInstance = new InMemoryInstance(TimelineClusterInfo.TIMELINE_METADATA.getMap(), backupCount, readBackupData, null, null);
		try {
			inMemoryManager.addMap(inMemoryInstance);
		} catch (ExceptionHazelcastDataStructureCreationFailure | ExceptionHazelcastDataStructureNotFoundFailure e) {
			logger.error("[TimelineSyncManager].init : error = " + e.getStackTrace());
		}
	}
	
	private TimelineExistStatus exists(String timeline) {
		//
		if (!timelineManager.containTimeline(timeline)) {
			try {
				Boolean isExist =  inMemoryManager.getMap(TimelineClusterInfo.TIMELINE_METADATA.getMap()).containsKey(timeline);
				if (isExist) {
					return TimelineExistStatus.CLUSTER;
				}
				else {
					return TimelineExistStatus.NONE;
				}
			} catch (ExceptionHazelcastDataStructureNotFoundFailure e) {
				//e.printStackTrace();
				logger.error("[TimelineSyncManager].isValidTimeline : error = " + e.getStackTrace());
				return TimelineExistStatus.ERROR;
			}
		}
		return TimelineExistStatus.LOCAL;
	}
	
	@SuppressWarnings("unchecked")
	private TreeMap<Long, String> getTimelineFromCluster(String timeline) {
		//
		Map<String, ServiceInstance> serviceInstances = serviceInstancesManager.getOnlineServiceInstancesWithoutMe();
		ServiceInstance selectedServiceInstance = serviceInstances.get((serviceInstances.keySet().toArray())[0]);
		
		String scheme = "http";
		
		String ip = selectedServiceInstance.getIp();
		Integer port = selectedServiceInstance.getPort();
		String url = scheme + "://" + ip + ":" + port.toString();
		
	    String path = "timeline-service/timelines/{timeline}";
	    path = path.replace("{timeline}", timeline);
	    
	    HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		
		String requestBody = "{\r\n" + 
							 "	\"type\" : \"get\"\r\n" + 
							 "}";
		
		// TODO : change method
		
		
		ResponseEntity<String> response = httpProxyAdaptor.executeHttpProxy(scheme, url, path, null, HttpMethod.GET, headers, requestBody);
		String responseBody = response.getBody();
		
		TreeMap<Long, String> retMap = new TreeMap<>();
		try {
			retMap = om.readValue(responseBody, TreeMap.class);
		} catch (JsonProcessingException e) {
			//e.printStackTrace();
			logger.error("[TimelineSyncManager].getTimelineFromCluster : error = " + e.getStackTrace());
			return retMap;
		}
		
		return retMap;
	}
	
	@Override
	public void addTimeline(String timeline) throws ExceptionTimelineConflictFailure, ExceptionTimelineInternalFailure {
		//
		TimelineExistStatus status = exists(timeline);
		
		if (status.equals(TimelineExistStatus.NONE) || status.equals(TimelineExistStatus.LOCAL)) {
			//
			String addedTimeline = null;
			try {
				addedTimeline = timelineManager.addTimeline(timeline);
			} catch (ExceptionTimelineConflictFailure e) {
				throw new ExceptionTimelineConflictFailure();
			}
			
			if (addedTimeline != null) {
				replicationManager.replicateAddTimeline(addedTimeline);
				
				TimelineMetadata timelineMetadata = new TimelineMetadata(addedTimeline, new Timestamp(System.currentTimeMillis()));
				try {
					inMemoryManager.getMap(TimelineClusterInfo.TIMELINE_METADATA.getMap()).put(addedTimeline, timelineMetadata);
				} catch (ExceptionHazelcastDataStructureNotFoundFailure e) {
					//e.printStackTrace();
					logger.error("[TimelineSyncManager].addTimeline : error = " + e.getStackTrace());
				}
			}
		}
		else if (status.equals(TimelineExistStatus.CLUSTER)) {
			//
			TreeMap<Long, String> values = getTimelineFromCluster(timeline);
			try {
				timelineManager.addTimeline(timeline);
				timelineManager.putAll(timeline, values);
			} catch (ExceptionTimelineConflictFailure | ExceptionTimelineResourceNotFound | ExceptionTimelineInternalFailure e) {
				//e.printStackTrace();
				logger.error("[TimelineSyncManager].addTimeline : error = " + e.getStackTrace());
				throw new ExceptionTimelineInternalFailure();
			}
			
			throw new ExceptionTimelineConflictFailure();
		}
		else {
			throw new ExceptionTimelineInternalFailure();
		}
	}

	@Override
	public void deleteTimeline(String timeline) throws ExceptionTimelineResourceNotFound {
		//
		String deletedTimeline = null;
		try {
			deletedTimeline = timelineManager.deleteTimeline(timeline);
		} catch (ExceptionTimelineResourceNotFound e) {
			throw new ExceptionTimelineResourceNotFound();
		}
		
		if (deletedTimeline != null) {
			replicationManager.replicateDeleteTimeline(deletedTimeline);
		}
	}
	
	@Override
	public void put(String timeline, Long time, String value) throws ExceptionTimelineResourceNotFound, ExceptionTimelineInternalFailure {
		//
		PutValue puttedValue = null;
		try {
			puttedValue = timelineManager.put(timeline, time, value);
		} catch (ExceptionTimelineResourceNotFound e) {
			throw new ExceptionTimelineResourceNotFound();
		} catch (ExceptionTimelineInternalFailure e) {
			throw new ExceptionTimelineInternalFailure();
		}
		
		if (puttedValue != null) {
			replicationManager.replicatePut(puttedValue);
		}
	}

	@Override
	public void putAll(String timeline, TreeMap<Long, String> values) throws ExceptionTimelineResourceNotFound, ExceptionTimelineInternalFailure {
		//
		PutValues puttedValues = null;
		try {
			puttedValues = timelineManager.putAll(timeline, values);
		} catch (ExceptionTimelineResourceNotFound e) {
			throw new ExceptionTimelineResourceNotFound();
		} catch (ExceptionTimelineInternalFailure e) {
			throw new ExceptionTimelineInternalFailure();
		}
		
		if (puttedValues != null) {
			replicationManager.replicatePutAll(puttedValues);
		}
	}

	@Override
	public void update(String timeline, Long time, String value) throws ExceptionTimelineResourceNotFound, ExceptionTimelineInternalFailure {
		//
		PutValue updatedValue = null;
		try {
			updatedValue = timelineManager.update(timeline, time, value);
		} catch (ExceptionTimelineResourceNotFound e) {
			throw new ExceptionTimelineResourceNotFound();
		} catch (ExceptionTimelineInternalFailure e) {
			throw new ExceptionTimelineInternalFailure();
		}
		
		if (updatedValue != null) {
			replicationManager.replicateUpdate(updatedValue);
		}
	}

	@Override
	public void updateAll(String timeline, TreeMap<Long, String> values) throws ExceptionTimelineResourceNotFound, ExceptionTimelineInternalFailure {
		//
		PutValues updatedValues = null;
		try {
			updatedValues = timelineManager.updateAll(timeline, values);
		} catch (ExceptionTimelineResourceNotFound e) {
			throw new ExceptionTimelineResourceNotFound();
		} catch (ExceptionTimelineInternalFailure e) {
			throw new ExceptionTimelineInternalFailure();
		}
		
		if (updatedValues != null) {
			replicationManager.replicateUpdateAll(updatedValues);
		}
	}

	@Override
	public void remove(String timeline, Long time) throws ExceptionTimelineResourceNotFound, ExceptionTimelineInternalFailure {
		//
		Remove removed = null;
		try {
			removed = timelineManager.remove(timeline, time);
		} catch (ExceptionTimelineResourceNotFound e) {
			throw new ExceptionTimelineResourceNotFound();
		} catch (ExceptionTimelineInternalFailure e) {
			throw new ExceptionTimelineInternalFailure();
		}
		
		if (removed != null) {
			replicationManager.replicateRemove(removed);
		}
	}

	@Override
	public void removeByTimes(String timeline, List<Long> times) throws ExceptionTimelineResourceNotFound, ExceptionTimelineInternalFailure {
		//
		RemoveByTimes removedByTimes = null;
		try {
			removedByTimes = timelineManager.removeByTimes(timeline, times);
		} catch (ExceptionTimelineResourceNotFound e) {
			throw new ExceptionTimelineResourceNotFound();
		} catch (ExceptionTimelineInternalFailure e) {
			throw new ExceptionTimelineInternalFailure();
		}
		
		if (removedByTimes != null) {
			replicationManager.replicateRemoveByTimes(removedByTimes);
		}
	}

	@Override
	public void removeByPeriod(String timeline, Long fromTime, Long toTime) throws ExceptionTimelineResourceNotFound {
		//
		RemoveByPeriod removedByPeriod = null;
		try {
			removedByPeriod = timelineManager.removeByPeriod(timeline, fromTime, toTime);
		} catch (ExceptionTimelineResourceNotFound e) {
			throw new ExceptionTimelineResourceNotFound();
		}
		
		if (removedByPeriod != null) {
			replicationManager.replicateRemoveByPeriod(removedByPeriod);
		}
	}

	@Override
	public void removeByBefore(String timeline, Long toTime) throws ExceptionTimelineResourceNotFound {
		//
		RemoveByBefore removedByBefore = null;
		try {
			removedByBefore = timelineManager.removeByBefore(timeline, toTime);
		} catch (ExceptionTimelineResourceNotFound e) {
			throw new ExceptionTimelineResourceNotFound();
		}
		
		if (removedByBefore != null) {
			replicationManager.replicateRemoveByBefore(removedByBefore);
		}
	}

	@Override
	public void clear(String timeline) throws ExceptionTimelineResourceNotFound, ExceptionTimelineInternalFailure {
		//
		String clearedTmeline = null;
		try {
			clearedTmeline = timelineManager.clear(timeline);
		} catch (ExceptionTimelineResourceNotFound e) {
			throw new ExceptionTimelineResourceNotFound();
		} catch (ExceptionTimelineInternalFailure e) {
			throw new ExceptionTimelineInternalFailure();
		}
		
		if (clearedTmeline != null) {
			replicationManager.replicateClear(clearedTmeline);
		}
	}
}