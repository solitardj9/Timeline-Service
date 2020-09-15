package com.solitardj9.timelineService.application.replicationManager.model;

public class ReplicationParamEnum {
    //
	public enum ReplicationTypeParamEnum {
	    //
		ADD_TIMELINE("addTimeline"),
		DELETE_TIMELINE("deleteTimeline"),
		PUT("put"),
		PUT_ALL("putAll"),
		UPDATE("update"),
		UPDATE_ALL("updateAll"),
		REMOVE("remove"),
		REMOVE_BY_TIMES("removeByTimes"),
		REMOVE_BY_PERIOD("removeByPeriod"),
		REMOVE_BY_BEFORE("removeByBefore"),
		CLEAR("clear")
		;
		
		private String type;
		
		private ReplicationTypeParamEnum(String type) {
			this.type = type;
	    }
	    
		public String getType() { 
	        return type;
	    }
	    
	    @Override
	    public String toString() {
	        return type;
	    }
	}
}