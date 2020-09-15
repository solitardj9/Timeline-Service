package com.solitardj9.timelineService.serviceInterface.timelineSyncManagerInterface.model;

public class ResponseTimeline extends ResponseDefualt {
	//
	private String timeline;
	
	public ResponseTimeline() {
		
	}

	public ResponseTimeline(String timeline) {
		this.timeline = timeline;
	}
	
	public ResponseTimeline(Integer status, String timeline) {
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
		return "ResponseTimeline [timeline=" + timeline + ", toString()=" + super.toString() + "]";
	}
}