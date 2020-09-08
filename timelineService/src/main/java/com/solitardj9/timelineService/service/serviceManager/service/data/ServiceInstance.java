package com.solitardj9.timelineService.service.serviceManager.service.data;

import java.io.Serializable;
import java.sql.Timestamp;

public class ServiceInstance implements Serializable {
	
	private static final long serialVersionUID = -8853014377660580778L;

	private String serviceInstanceName;
	
	private Timestamp registeredTime;
	
	public ServiceInstance() {
		
	}

	public ServiceInstance(String serviceInstanceName, Timestamp registeredTime) {
		this.serviceInstanceName = serviceInstanceName;
		this.registeredTime = registeredTime;
	}

	public String getServiceInstanceName() {
		return serviceInstanceName;
	}

	public void setServiceInstanceName(String serviceInstanceName) {
		this.serviceInstanceName = serviceInstanceName;
	}

	public Timestamp getRegisteredTime() {
		return registeredTime;
	}

	public void setRegisteredTime(Timestamp registeredTime) {
		this.registeredTime = registeredTime;
	}

	@Override
	public String toString() {
		return "ServiceInstance [serviceInstanceName=" + serviceInstanceName + ", registeredTime=" + registeredTime + "]";
	}
}