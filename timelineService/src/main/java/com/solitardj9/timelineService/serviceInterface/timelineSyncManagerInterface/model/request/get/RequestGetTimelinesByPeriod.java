package com.solitardj9.timelineService.serviceInterface.timelineSyncManagerInterface.model.request.get;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("getByPeriod")
public class RequestGetTimelinesByPeriod extends RequestGetTimelinesDefault {
	//
	private Long fromTime;
	
	private Long toTime;
	
	public RequestGetTimelinesByPeriod() {
		
	}
	
	public RequestGetTimelinesByPeriod(Long fromTime, Long toTime) {
		this.fromTime = fromTime;
		this.toTime = toTime;
	}

	public RequestGetTimelinesByPeriod(String type, Long fromTime, Long toTime) {
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
		return "RequestGetTimelinesByPeriod [fromTime=" + fromTime + ", toTime=" + toTime + ", toString()="
				+ super.toString() + "]";
	}
}