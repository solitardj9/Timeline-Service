package com.solitardj9.timelineService.application.timelineManager.model;

public class PutValue {
	//
	private String timeline;
	
	private Long timestamp;
	
	private String value;
	
	public PutValue() {
		
	}

	public PutValue(String timeline, Long timestamp, String value) {
		this.timeline = timeline;
		this.timestamp = timestamp;
		this.value = value;
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

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "PutValue [timeline=" + timeline + ", timestamp=" + timestamp + ", value=" + value + "]";
	}
}