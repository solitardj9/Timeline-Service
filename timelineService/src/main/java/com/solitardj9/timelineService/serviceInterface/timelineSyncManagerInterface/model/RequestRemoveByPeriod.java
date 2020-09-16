package com.solitardj9.timelineService.serviceInterface.timelineSyncManagerInterface.model;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("removeByPeriod")
public class RequestRemoveByPeriod extends RequestDeleteDefault {
	//
	private String timeline;
	
	private Long fromTime;
	
	private Long toTime;
	
	public RequestRemoveByPeriod() {
		
	}

	public RequestRemoveByPeriod(String timeline) {
		this.timeline = timeline;
	}
	
	public String getTimeline() {
		return timeline;
	}

	public void setTimeline(String timeline) {
		this.timeline = timeline;
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
		return "RequestRemove [timeline=" + timeline + ", fromTime=" + fromTime + ", toTime=" + toTime + ", toString()="
				+ super.toString() + "]";
	}
}