package com.solitardj9.timelineService.service.serviceInstancesManager.model;

public class ServiceInstanceParamEnum {
    //
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
	
	public enum ServiceInstanceStatus {
	    //
		ONLINE("online"),
		OFFLINE("offline")
		;
		
		private String status;
		
		private ServiceInstanceStatus(String status) {
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