package com.solitardj9.timelineService.serviceInterface.timelineSyncManagerInterface.model.request.remove;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("removeByBefore")
public class RequestRemoveByBefore extends RequestRemoveDefault {
	//
	private Long toTime;
	
	public RequestRemoveByBefore() {
		
	}

	public RequestRemoveByBefore(Long toTime) {
		this.toTime = toTime;
	}

	public RequestRemoveByBefore(String type, Long toTime) {
		setType(type);
		this.toTime = toTime;
	}

	public Long getToTime() {
		return toTime;
	}

	public void setToTime(Long toTime) {
		this.toTime = toTime;
	}

	@Override
	public String toString() {
		return "RequestRemoveByBefore [toTime=" + toTime + ", toString()=" + super.toString()
				+ "]";
	}
}