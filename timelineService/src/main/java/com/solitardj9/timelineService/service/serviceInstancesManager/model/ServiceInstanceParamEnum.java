package com.solitardj9.timelineService.service.serviceInstancesManager.model;

public class ServiceInstanceParamEnum {
    //
	public enum ServiceInstanceInMemoryMap {
	    //
		SERVICE_INSTANCE("serviceInstance")
		;
		
		private String mapName;
		
		private ServiceInstanceInMemoryMap(String mapName) {
			this.mapName = mapName;
	    }
	    
		public String getMapName() { 
	        return mapName;
	    }
	    
	    @Override
	    public String toString() {
	        return mapName;
	    }
	}
}