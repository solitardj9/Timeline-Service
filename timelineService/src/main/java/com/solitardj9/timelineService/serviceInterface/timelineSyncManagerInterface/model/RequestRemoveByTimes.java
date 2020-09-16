package com.solitardj9.timelineService.serviceInterface.timelineSyncManagerInterface.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("removeByTimes")
public class RequestRemoveByTimes extends RequestDeleteDefault {
	//
	private String timeline;
	
	private List<Long> times;
	
	public RequestRemoveByTimes() {
		
	}

	public RequestRemoveByTimes(String timeline) {
		this.timeline = timeline;
	}
	
	public String getTimeline() {
		return timeline;
	}

	public void setTimeline(String timeline) {
		this.timeline = timeline;
	}

	public List<Long> getTimes() {
		return times;
	}

	public void setTimes(List<Long> times) {
		this.times = times;
	}

	@Override
	public String toString() {
		return "RequestRemoveByTimes [timeline=" + timeline + ", times=" + times + ", toString()=" + super.toString()
				+ "]";
	}
}