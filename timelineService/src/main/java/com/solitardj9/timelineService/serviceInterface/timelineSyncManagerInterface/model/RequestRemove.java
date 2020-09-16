package com.solitardj9.timelineService.serviceInterface.timelineSyncManagerInterface.model;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("remove")
public class RequestRemove extends RequestDeleteDefault {
	//
	private String timeline;
	
	private Long time;
	
	public RequestRemove() {
		
	}

	public RequestRemove(String timeline) {
		this.timeline = timeline;
	}
	
	public String getTimeline() {
		return timeline;
	}

	public void setTimeline(String timeline) {
		this.timeline = timeline;
	}
	
	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return "RequestRemove [timeline=" + timeline + ", time=" + time + ", toString()=" + super.toString() + "]";
	}
}