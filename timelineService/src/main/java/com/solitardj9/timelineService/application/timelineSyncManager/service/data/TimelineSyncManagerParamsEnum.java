package com.solitardj9.timelineService.application.timelineSyncManager.service.data;

public class TimelineSyncManagerParamsEnum {
	//
	public enum TimelineClusterInfo {
		TIMELINE_METADATA("timelineMetadata")
		;
		
		private String map;
		
		private TimelineClusterInfo(String map) {
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
	
	public enum TimelineExistStatus {
		CLUSTER("cluster"),
		LOCAL("local"),
		NONE("none"),
		ERROR("error")
		;
		
		private String status;
		
		private TimelineExistStatus(String status) {
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