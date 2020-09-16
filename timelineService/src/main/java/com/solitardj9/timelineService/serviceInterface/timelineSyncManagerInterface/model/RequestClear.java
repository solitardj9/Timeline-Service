package com.solitardj9.timelineService.serviceInterface.timelineSyncManagerInterface.model;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("clear")
public class RequestClear extends RequestDeleteDefault {
	//
	private String timeline;
	
	public RequestClear() {
		
	}

	public RequestClear(String timeline) {
		this.timeline = timeline;
	}
	
	public String getTimeline() {
		return timeline;
	}

	public void setTimeline(String timeline) {
		this.timeline = timeline;
	}

	@Override
	public String toString() {
		return "RequestDeleteClear [timeline=" + timeline + ", toString()=" + super.toString() + "]";
	}
}