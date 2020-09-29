package com.solitardj9.timelineService.application.timelineSyncManager.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.concurrent.ConcurrentNavigableMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solitardj9.timelineService.application.messageFlowManager.model.ConsumeMessage;
import com.solitardj9.timelineService.application.timelineManager.model.Remove;
import com.solitardj9.timelineService.application.timelineManager.model.RemoveByBefore;
import com.solitardj9.timelineService.application.timelineManager.model.RemoveByPeriod;
import com.solitardj9.timelineService.application.timelineManager.model.RemoveByTimes;
import com.solitardj9.timelineService.application.timelineManager.service.TimelineManager;
import com.solitardj9.timelineService.application.timelineManager.service.exception.ExceptionTimelineInternalFailure;
import com.solitardj9.timelineService.application.timelineManager.service.exception.ExceptionTimelineResourceNotFound;
import com.solitardj9.timelineService.application.timelineSyncManager.model.RestoreMessage;
import com.solitardj9.timelineService.application.timelineSyncManager.service.TimelineRestoreManager;
import com.solitardj9.timelineService.service.serviceInstancesManager.service.ServiceInstancesManager;
import com.solitardj9.timelineService.service.serviceInstancesManager.service.data.ServiceInstance;
import com.solitardj9.timelineService.service.serviceManager.service.ServiceManager;
import com.solitardj9.timelineService.systemInterface.httpInterface.service.HttpProxyAdaptor;
import com.solitardj9.timelineService.utils.fileUtil.MapFileUtil;

@Service("timelineRestoreManager")
public class TimelineRestoreManagerImpl implements TimelineRestoreManager {
	
	private static final Logger logger = LoggerFactory.getLogger(TimelineRestoreManagerImpl.class);
	
	@Autowired
	ServiceManager serviceManager;
	
	@Autowired
	ServiceInstancesManager serviceInstancesManager;
	
	@Autowired
	TimelineManager timelineManager;
	
	@Autowired
	HttpProxyAdaptor httpProxyAdaptor;
	
	private List<ConsumeMessage> consumeMessages = new ArrayList<>();
	
	private Random random = new Random();
	
	private ObjectMapper om = new ObjectMapper();
	
	@EventListener
	@Async
	public void onRestoreMessage(RestoreMessage restoreMessage) {
		//
		executeRestore(); 
	}
	
	@Override
	public void testRestore() {
		//
		String scheme = "http";
		
		String ip = "127.0.0.1";
		Integer port = 18771;
		
		String url = ip + ":" + port.toString();
		
		String fileName = "backup_" + serviceManager.getServiceName() + "_" + String.valueOf(System.currentTimeMillis());
		
		File file = getBackupFileFromCluster(scheme, url, fileName);
		
		deleteBackupFileInCluster(scheme, url, fileName);
		
		timelineManager.clearTimelines();
		
		timelineManager.restoreTimelines(file);
		
		Map<String, ConcurrentNavigableMap<Long, String>> timelines = timelineManager.getTimelines();
		for (Entry<String, ConcurrentNavigableMap<Long, String>> timeline : timelines.entrySet()) {
			//
			logger.info("[TimelineRestoreManager].testRestore : timeline name = " + timeline.getKey());
			logger.info("[TimelineRestoreManager].testRestore : timeline values = " + timeline.getValue().toString());
		}
		
		MapFileUtil.deleteFile(file);
	}
	
	@Override
	public void executeRestore() {
		//
		Map<String, ServiceInstance> serviceInstances = serviceInstancesManager.getOnlineServiceInstancesWithoutMe();
		if (!serviceInstances.isEmpty()) {
			ServiceInstance selectedServiceInstance = serviceInstances.get((serviceInstances.keySet().toArray())[getRandomIndex(serviceInstances.size())]);
			
			String scheme = "http";
			
			String ip = selectedServiceInstance.getIp();
			Integer port = selectedServiceInstance.getPort();
			
			String url = ip + ":" + port.toString();
			
			String fileName = "backup_" + serviceManager.getServiceName() + "_" + String.valueOf(System.currentTimeMillis());
			
			File file = getBackupFileFromCluster(scheme, url, fileName);
			
			deleteBackupFileInCluster(scheme, url, fileName);
			
			timelineManager.restoreTimelines(file);
			
			MapFileUtil.deleteFile(file);
		}
	}
	
	private File getBackupFileFromCluster(String scheme, String url, String fileName) {
		//
		String path = "timeline-service/timelines/backup/{fileName}";
		path = path.replace("{fileName}", fileName);
		
		File file = httpProxyAdaptor.executeHttpProxyForFile(scheme, url, path, null, null, null, fileName);

		System.out.println(file.getName());
		System.out.println(file);
		
		return file;
	}
	
	private void deleteBackupFileInCluster(String scheme, String url, String fileName) {
		//
		String path = "timeline-service/timelines/backup/{fileName}";
		path = path.replace("{fileName}", fileName);
		
		httpProxyAdaptor.executeHttpProxy(scheme, url, path, null, HttpMethod.DELETE, null, null);
	}
	
	private Integer getRandomIndex(Integer bound/*exclusive*/) {
		//
		if (bound == 0)
			return 0;
		return random.nextInt(bound);
	}

	@Override
	public void addReplicationEvent(ConsumeMessage consumeMessage) {
		//
		consumeMessages.add(consumeMessage);
	}

	@Override
	public void restoreReplicationEvent() {
		//
		for (ConsumeMessage consumeMessage : consumeMessages ) {
			//
			if (consumeMessage.getType().equals("deleteTimeline")) {
				String timeline = consumeMessage.getMessage();
				try {
					timelineManager.deleteTimeline(timeline);
				} catch (ExceptionTimelineResourceNotFound e) {
					logger.error("[TimelineRestoreManager].restoreReplicationEvent - deleteTimeline : error = " + e);
				}
			}
			else if (consumeMessage.getType().equals("remove")) {
				String strRemove = consumeMessage.getMessage();
				try {
					Remove remove = om.readValue(strRemove, Remove.class);
					timelineManager.remove(remove.getTimeline(), remove.getTimestamp());
				} catch (ExceptionTimelineResourceNotFound | ExceptionTimelineInternalFailure | JsonProcessingException e) {
					logger.error("[TimelineRestoreManager].restoreReplicationEvent - remove : error = " + e);
				}
			}
			else if (consumeMessage.getType().equals("removeByTimes")) {
				String strRemoveByTimes = consumeMessage.getMessage();
				try {
					RemoveByTimes removeByTimes = om.readValue(strRemoveByTimes, RemoveByTimes.class);
					timelineManager.removeByTimes(removeByTimes.getTimeline(), removeByTimes.getTimestamps());
				} catch (ExceptionTimelineResourceNotFound | ExceptionTimelineInternalFailure | JsonProcessingException e) {
					logger.error("[TimelineRestoreManager].restoreReplicationEvent - removeByTimes : error = " + e);
				}
			}
			else if (consumeMessage.getType().equals("removeByPeriod")) {
				String strRemoveByPeriod = consumeMessage.getMessage();
				try {
					RemoveByPeriod removeByPeriod = om.readValue(strRemoveByPeriod, RemoveByPeriod.class);
					timelineManager.removeByPeriod(removeByPeriod.getTimeline(), removeByPeriod.getFromTimestamp(), removeByPeriod.getToTimestamp());
				} catch (ExceptionTimelineResourceNotFound | JsonProcessingException e) {
					logger.error("[TimelineRestoreManager].restoreReplicationEvent - removeByPeriod : error = " + e);
				}
			}
			else if (consumeMessage.getType().equals("removeByBefore")) {
				String strRemoveByBefore = consumeMessage.getMessage();
				try {
					RemoveByBefore removeByBefore = om.readValue(strRemoveByBefore, RemoveByBefore.class);
					timelineManager.removeByBefore(removeByBefore.getTimeline(), removeByBefore.getToTimestamp());
				} catch (ExceptionTimelineResourceNotFound | JsonProcessingException e) {
					logger.error("[TimelineRestoreManager].restoreReplicationEvent - removeByBefore : error = " + e);
				}
			}
			else if (consumeMessage.getType().equals("clear")) {
				String timeline = consumeMessage.getMessage();
				try {
					timelineManager.clear(timeline);
				} catch (ExceptionTimelineResourceNotFound | ExceptionTimelineInternalFailure  e) {
					logger.error("[TimelineRestoreManager].restoreReplicationEvent - clear : error = " + e);
				}
			}
			else {
				logger.error("[TimelineRestoreManager].restoreReplicationEvent : error = not supported type. (" + consumeMessage.getType() + ")");
			}
		}
	}
}