package com.solitardj9.timelineService.serviceInterface.serviceManagerInterface.model;

import java.sql.Timestamp;

public class ServiceHealth {
	//
	private String serviceName;
	
	private Timestamp timestamp;
	
	private Boolean isRegistered;

	public ServiceHealth() {
	}
	
	public ServiceHealth(String serviceName, Timestamp timestamp, Boolean isRegistered) {
		this.serviceName = serviceName;
		this.timestamp = timestamp;
		this.isRegistered = isRegistered;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public Boolean getIsRegistered() {
		return isRegistered;
	}

	public void setIsRegistered(Boolean isRegistered) {
		this.isRegistered = isRegistered;
	}

	@Override
	public String toString() {
		return "ServiceHealth [serviceName=" + serviceName + ", timestamp=" + timestamp + ", isRegistered="
				+ isRegistered + "]";
	}
}