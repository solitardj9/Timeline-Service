package com.solitardj9.timelineService.serviceInterface.serviceManagerInterface.model;

import java.sql.Timestamp;

import com.solitardj9.timelineService.service.serviceInstancesManager.model.ServiceInstanceParamEnum.ServiceInstanceClusterStatus;
import com.solitardj9.timelineService.service.serviceInstancesManager.model.ServiceInstanceParamEnum.ServiceInstanceRegisterStatus;

public class ServiceHealth {
	//
	private String serviceName;
	
	private Timestamp timestamp;
	
	private ServiceInstanceRegisterStatus registerStatus;
	
	private ServiceInstanceClusterStatus clusterStatus;

	public ServiceHealth() {
	}
	
	public ServiceHealth(String serviceName, Timestamp timestamp, ServiceInstanceRegisterStatus registerStatus, ServiceInstanceClusterStatus clusterStatus) {
		this.serviceName = serviceName;
		this.timestamp = timestamp;
		this.registerStatus = registerStatus;
		this.clusterStatus = clusterStatus;
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

	public ServiceInstanceRegisterStatus getRegisterStatus() {
		return registerStatus;
	}

	public void setRegisterStatus(ServiceInstanceRegisterStatus registerStatus) {
		this.registerStatus = registerStatus;
	}

	public ServiceInstanceClusterStatus getClusterStatus() {
		return clusterStatus;
	}

	public void setClusterStatus(ServiceInstanceClusterStatus clusterStatus) {
		this.clusterStatus = clusterStatus;
	}

	@Override
	public String toString() {
		return "ServiceHealth [serviceName=" + serviceName + ", timestamp=" + timestamp + ", registerStatus="
				+ registerStatus + ", clusterStatus=" + clusterStatus + "]";
	}
}