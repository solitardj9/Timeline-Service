package com.solitardj9.timelineService.application.timelineManager.model;

public class RemoveByBefore {
	//
	private String timeline;
	
	private Long toTimestamp;
	
	public RemoveByBefore() {
		
	}

	public RemoveByBefore(String timeline, Long toTimestamp) {
		this.timeline = timeline;
		this.toTimestamp = toTimestamp;
	}

	public String getTimeline() {
		return timeline;
	}

	public void setTimeline(String timeline) {
		this.timeline = timeline;
	}

	public Long getToTimestamp() {
		return toTimestamp;
	}

	public void setToTimestamp(Long toTimestamp) {
		this.toTimestamp = toTimestamp;
	}

	@Override
	public String toString() {
		return "RemoveByPeriod [timeline=" + timeline + ", toTimestamp=" + toTimestamp + "]";
	}
}