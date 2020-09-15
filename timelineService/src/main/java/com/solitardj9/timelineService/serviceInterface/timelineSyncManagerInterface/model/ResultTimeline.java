package com.solitardj9.timelineService.serviceInterface.timelineSyncManagerInterface.model;

public class ResultTimeline extends ResultDefualt {
	//
	private String timeline;
	
	public ResultTimeline() {
		
	}

	public ResultTimeline(String timeline) {
		this.timeline = timeline;
	}
	
	public ResultTimeline(Integer status, String timeline) {
		this.timeline = timeline;
		setStatus(status);
	}

	public String getTimeline() {
		return timeline;
	}

	public void setTimeline(String timeline) {
		this.timeline = timeline;
	}

	@Override
	public String toString() {
		return "ResultTimeline [timeline=" + timeline + ", toString()=" + super.toString() + "]";
	}
}