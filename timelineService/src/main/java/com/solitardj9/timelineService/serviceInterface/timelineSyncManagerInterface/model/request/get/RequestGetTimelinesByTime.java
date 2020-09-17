package com.solitardj9.timelineService.serviceInterface.timelineSyncManagerInterface.model.request.get;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("getByTime")
public class RequestGetTimelinesByTime extends RequestGetTimelinesDefault {
	//
	private Long time;
	
	public RequestGetTimelinesByTime() {
		
	}

	public RequestGetTimelinesByTime(Long time) {
		this.time = time;
	}

	public RequestGetTimelinesByTime(String type, Long time) {
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
		return "RequestGetTimelinesByTime [time=" + time + ", toString()=" + super.toString()
				+ "]";
	}
}