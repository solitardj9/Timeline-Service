package com.solitardj9.timelineService.serviceInterface.timelineSyncManagerInterface.model.request.get;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("getByTime")
public class RequestGetByTime extends RequestGetDefault {
	//
	private Long time;
	
	public RequestGetByTime() {
		
	}

	public RequestGetByTime(Long time) {
		this.time = time;
	}

	public RequestGetByTime(String type, Long time) {
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
		return "RequestRemoveByBefore [time=" + time + ", toString()=" + super.toString()
				+ "]";
	}
}