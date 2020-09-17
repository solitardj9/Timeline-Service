package com.solitardj9.timelineService.serviceInterface.timelineSyncManagerInterface.model.request.remove;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("removeByPeriod")
public class RequestRemoveByPeriod extends RequestRemoveDefault {
	//
	private Long fromTime;
	
	private Long toTime;
	
	public RequestRemoveByPeriod() {
		
	}
	
	public RequestRemoveByPeriod(Long fromTime, Long toTime) {
		this.fromTime = fromTime;
		this.toTime = toTime;
	}

	public RequestRemoveByPeriod(String type, Long fromTime, Long toTime) {
		setType(type);
		this.fromTime = fromTime;
		this.toTime = toTime;
	}

	public Long getFromTime() {
		return fromTime;
	}

	public void setFromTime(Long fromTime) {
		this.fromTime = fromTime;
	}

	public Long getToTime() {
		return toTime;
	}

	public void setToTime(Long toTime) {
		this.toTime = toTime;
	}

	@Override
	public String toString() {
		return "RequestRemoveByPeriod [fromTime=" + fromTime + ", toTime=" + toTime + ", toString()="
				+ super.toString() + "]";
	}
}