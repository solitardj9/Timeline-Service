package com.solitardj9.timelineService.serviceInterface.timelineSyncManagerInterface.model.request.get;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("getByPeriod")
public class RequestGetByPeriod extends RequestGetDefault {
	//
	private Long fromTime;
	
	private Long toTime;
	
	public RequestGetByPeriod() {
		
	}
	
	public RequestGetByPeriod(Long fromTime, Long toTime) {
		this.fromTime = fromTime;
		this.toTime = toTime;
	}

	public RequestGetByPeriod(String type, Long fromTime, Long toTime) {
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
		return "RequestGetByPeriod [fromTime=" + fromTime + ", toTime=" + toTime + ", toString()="
				+ super.toString() + "]";
	}
}