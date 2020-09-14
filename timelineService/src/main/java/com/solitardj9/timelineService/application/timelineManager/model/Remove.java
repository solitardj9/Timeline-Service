package com.solitardj9.timelineService.application.timelineManager.model;

public class Remove {
	//
	private String timeline;
	
	private Long timestamp;
	
	public Remove() {
		
	}

	public Remove(String timeline, Long timestamp) {
		this.timeline = timeline;
		this.timestamp = timestamp;
	}

	public String getTimeline() {
		return timeline;
	}

	public void setTimeline(String timeline) {
		this.timeline = timeline;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		return "Remove [timeline=" + timeline + ", timestamp=" + timestamp + "]";
	}
}