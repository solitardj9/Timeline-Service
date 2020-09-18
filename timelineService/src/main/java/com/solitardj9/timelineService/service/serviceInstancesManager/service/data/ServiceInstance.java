package com.solitardj9.timelineService.service.serviceInstancesManager.service.data;

import java.io.Serializable;
import java.sql.Timestamp;

public class ServiceInstance implements Serializable {
	
	private static final long serialVersionUID = -8853014377660580778L;

	private String serviceInstanceName;
	
	private String ip;
	
	private Integer port;
	
	private Timestamp registeredTime;
	
	private String status;
	
	private Timestamp updatedTime;
	
	public ServiceInstance() {
		
	}

	public ServiceInstance(String serviceInstanceName, String ip, Integer port, Timestamp registeredTime, String status, Timestamp updatedTime) {
		this.serviceInstanceName = serviceInstanceName;
		this.ip = ip;
		this.port = port;
		this.registeredTime = registeredTime;
		this.status = status;
		this.updatedTime = updatedTime;
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

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Timestamp getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Timestamp updatedTime) {
		this.updatedTime = updatedTime;
	}

	@Override
	public String toString() {
		return "ServiceInstance [serviceInstanceName=" + serviceInstanceName + ", ip=" + ip + ", port=" + port
				+ ", registeredTime=" + registeredTime + ", status=" + status + ", updatedTime=" + updatedTime + "]";
	}
}