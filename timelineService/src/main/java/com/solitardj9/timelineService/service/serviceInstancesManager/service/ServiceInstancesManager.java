package com.solitardj9.timelineService.service.serviceInstancesManager.service;

import java.util.Map;

import com.solitardj9.timelineService.service.serviceInstancesManager.model.ServiceInstanceParamEnum.ServiceInstanceClusterStatus;
import com.solitardj9.timelineService.service.serviceInstancesManager.model.ServiceInstanceParamEnum.ServiceInstanceRegisterStatus;
import com.solitardj9.timelineService.service.serviceInstancesManager.service.data.ServiceInstance;

public interface ServiceInstancesManager {
	//
	public void setServiceInstancesCallback(ServiceInstancesCallback serviceInstancesCallback);
	
	public void registerService(String serviceName);
	
	public void unregisterService(String serviceName);
	
	public Map<String, ServiceInstance> getServiceInstances();
	
	/**
	 * get other nodes in cluster which is online status  
	 * @return
	 */
	public Map<String, ServiceInstance> getOnlineServiceInstancesWithoutMe();
	
	public void checkHealth();
	
	public ServiceInstanceRegisterStatus isRegistered();
	
	public ServiceInstanceClusterStatus isClustered();
}