package com.solitardj9.timelineService.service.serviceInstancesManager.service.data;

import java.io.Serializable;
import java.sql.Timestamp;

public class ServiceInstance implements Serializable {
	
	private static final long serialVersionUID = -8853014377660580778L;

	private String serviceInstanceName;
	
	private String ip;
	
	private Integer port;
	
	private Timestamp registeredTime;
	
	public ServiceInstance() {
		
	}

	public ServiceInstance(String serviceInstanceName, String ip, Integer port, Timestamp registeredTime) {
		this.serviceInstanceName = serviceInstanceName;
		this.ip = ip;
		this.port = port;
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
		return "ServiceInstance [serviceInstanceName=" + serviceInstanceName + ", ip=" + ip + ", port=" + port
				+ ", registeredTime=" + registeredTime + "]";
	}
}