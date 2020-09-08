package com.solitardj9.timelineService.service.serviceInstancesManager.service;

import java.util.Map;

import com.solitardj9.timelineService.service.serviceInstancesManager.service.data.ServiceInstance;

public interface ServiceInstancesManager {
	//
	public void setServiceInstancesCallback(ServiceInstancesCallback serviceInstancesCallback);
	
	public void registerService(String serviceName);
	
	public void unregisterService(String serviceName);
	
	public Map<String, ServiceInstance> getServiceInstances();
}