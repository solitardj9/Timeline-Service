package com.solitardj9.timelineService.serviceInterface.timelineSyncManagerInterface.model.request.get;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("get")
public class RequestGetTimelines extends RequestGetTimelinesDefault {
	//
	public RequestGetTimelines() {
		
	}
	
	public RequestGetTimelines(String type) {
		setType(type);
	}

	@Override
	public String toString() {
		return "RequestGetTimelines [toString()=" + super.toString() + "]";
	}
}