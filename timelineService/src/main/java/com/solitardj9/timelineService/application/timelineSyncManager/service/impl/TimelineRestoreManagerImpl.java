package com.solitardj9.timelineService.application.timelineSyncManager.service.impl;

import java.io.File;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.solitardj9.timelineService.application.timelineSyncManager.model.RestoreMessage;
import com.solitardj9.timelineService.application.timelineSyncManager.service.TimelineRestoreManager;
import com.solitardj9.timelineService.service.serviceInstancesManager.service.ServiceInstancesManager;
import com.solitardj9.timelineService.service.serviceInstancesManager.service.data.ServiceInstance;
import com.solitardj9.timelineService.service.serviceManager.service.ServiceManager;
import com.solitardj9.timelineService.systemInterface.httpInterface.service.HttpProxyAdaptor;

@Service("timelineRestoreManager")
public class TimelineRestoreManagerImpl implements TimelineRestoreManager {
	
	private static final Logger logger = LoggerFactory.getLogger(TimelineRestoreManagerImpl.class);
	
	@Autowired
	ServiceManager serviceManager;
	
	@Autowired
	ServiceInstancesManager serviceInstancesManager;
	
	@Autowired
	HttpProxyAdaptor httpProxyAdaptor;
	
	private Random random = new Random();
	
	@EventListener
	@Async
	public void onRestoreMessage(RestoreMessage restoreMessage) {
		//
		executeRestore(); 
	}
	
	@Override
	public void testRestore() {
		//
		executeRestore();
	}
	
	private void executeRestore() {
		//
		Map<String, ServiceInstance> serviceInstances = serviceInstancesManager.getOnlineServiceInstancesWithoutMe();
		
		//ServiceInstance selectedServiceInstance = serviceInstances.get((serviceInstances.keySet().toArray())[getRandomIndex(serviceInstances.size())]);
		
		String scheme = "http";
		
		//String ip = selectedServiceInstance.getIp();
		//Integer port = selectedServiceInstance.getPort();
		String ip = "127.0.0.1";
		Integer port = 18771;
		String url = ip + ":" + port.toString();
		
		String fileName = "backup_" + serviceManager.getServiceName() + "_" + String.valueOf(System.currentTimeMillis());
		
		String path = "timeline-service/timelines/backup/{fileName}";
		path = path.replace("{fileName}", fileName);
		
		File file = httpProxyAdaptor.executeHttpProxyForFile(scheme, url, path, null, null, null, fileName);

		System.out.println(file.getName());
		System.out.println(file);
		
		// TODO :
		// 1. 템프 파일 백업 받고
		// 백업 퓨ㅏ일 지우고(원격)
		// 타임라인 전체 삭제하고 (임시)
		// 복구하고
		// 템프파일 삭제하고
		// 결과 출력해볼것
		
	}
	
	private Integer getRandomIndex(Integer bound/*exclusive*/) {
		//
		if (bound == 0)
			return 0;
		return random.nextInt(bound);
	}
}