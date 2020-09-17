package com.solitardj9.timelineService.serviceInterface.timelineSyncManagerInterface.model.request.get;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("get")
public class RequestGet extends RequestGetDefault {
	//
	public RequestGet() {
		
	}
	
	public RequestGet(String type) {
		setType(type);
	}

	@Override
	public String toString() {
		return "RequestGet [toString()=" + super.toString() + "]";
	}
}