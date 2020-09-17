package com.solitardj9.timelineService.serviceInterface.timelineSyncManagerInterface.model.request.remove;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("remove")
public class RequestRemove extends RequestRemoveDefault {
	//
	private Long time;
	
	public RequestRemove() {
		
	}

	public RequestRemove(Long time) {
		this.time = time;
	}
	
	public RequestRemove(String type, Long time) {
		setType(type);
		this.time = time;
	}
	
	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return "RequestRemove [time=" + time + ", toString()=" + super.toString() + "]";
	}
}