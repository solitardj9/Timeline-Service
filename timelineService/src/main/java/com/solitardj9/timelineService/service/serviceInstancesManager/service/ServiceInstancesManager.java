package com.solitardj9.timelineService.service.serviceInstancesManager.service;

import java.util.Map;

import com.solitardj9.timelineService.service.serviceManager.service.data.ServiceInstance;

public interface ServiceInstancesManager {
	//
	public void registerService(String serviceName);
	
	public void unregisterService(String serviceName);
	
	public Map<String, ServiceInstance> getServiceInstances();
}