package com.solitardj9.timelineService.serviceInterface.timelineSyncManagerInterface.model.request.remove;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("clear")
public class RequestClear extends RequestRemoveDefault {
	//
	public RequestClear() {
		
	}
	
	public RequestClear(String type) {
		setType(type);
	}

	@Override
	public String toString() {
		return "RequestDeleteClear [toString()=" + super.toString() + "]";
	}
}