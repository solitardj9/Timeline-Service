package com.solitardj9.timelineService.service.serviceInstancesManager.model;

import java.io.Serializable;

public class ServiceInstanceParamEnum implements Serializable {

	private static final long serialVersionUID = -3957717723362927783L;

	public enum ServiceInstanceInMemoryMap {
	    //
		SERVICE_INSTANCE("serviceInstance")
		;
		
		private String map;
		
		private ServiceInstanceInMemoryMap(String map) {
			this.map = map;
	    }
	    
		public String getMap() { 
	        return map;
	    }
	    
	    @Override
	    public String toString() {
	        return map;
	    }
	}
	
	public enum ServiceInstanceRegisterStatus {
	    //
		UNREGISTERED("unregistered"),
		REGISTERED("registered")
		;
		
		private String status;
		
		private ServiceInstanceRegisterStatus(String status) {
			this.status = status;
	    }
	    
		public String getStatus() { 
	        return status;
	    }
	    
	    @Override
	    public String toString() {
	        return status;
	    }
	}
	
	public enum ServiceInstanceClusterStatus {
	    //
		OFFLINE("offline"),
		RESTORED("restored"),
		ONLINE("online")
		;
		
		private String status;
		
		private ServiceInstanceClusterStatus(String status) {
			this.status = status;
	    }
	    
		public String getStatus() { 
	        return status;
	    }
	    
	    @Override
	    public String toString() {
	        return status;
	    }
	}
}