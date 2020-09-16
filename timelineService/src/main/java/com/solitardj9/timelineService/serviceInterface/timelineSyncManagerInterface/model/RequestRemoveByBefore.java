package com.solitardj9.timelineService.serviceInterface.timelineSyncManagerInterface.model;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("removeByBefore")
public class RequestRemoveByBefore extends RequestDeleteDefault {
	//
	private String timeline;
	
	private Long toTime;
	
	public RequestRemoveByBefore() {
		
	}

	public RequestRemoveByBefore(String timeline) {
		this.timeline = timeline;
	}
	
	public String getTimeline() {
		return timeline;
	}

	public void setTimeline(String timeline) {
		this.timeline = timeline;
	}
	
	public Long getToTime() {
		return toTime;
	}

	public void setToTime(Long toTime) {
		this.toTime = toTime;
	}

	@Override
	public String toString() {
		return "RequestRemoveByBefore [timeline=" + timeline + ", toTime=" + toTime + ", toString()=" + super.toString()
				+ "]";
	}
}