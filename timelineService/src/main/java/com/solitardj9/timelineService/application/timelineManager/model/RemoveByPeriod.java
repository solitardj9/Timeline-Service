package com.solitardj9.timelineService.application.timelineManager.model;

public class RemoveByPeriod {
	//
	private String timeline;
	
	private Long fromTimestamp;
	
	private Long toTimestamp;
	
	public RemoveByPeriod() {
		
	}

	public RemoveByPeriod(String timeline, Long fromTimestamp, Long toTimestamp) {
		this.timeline = timeline;
		this.fromTimestamp = fromTimestamp;
		this.toTimestamp = toTimestamp;
	}

	public String getTimeline() {
		return timeline;
	}

	public void setTimeline(String timeline) {
		this.timeline = timeline;
	}

	public Long getFromTimestamp() {
		return fromTimestamp;
	}

	public void setFromTimestamp(Long fromTimestamp) {
		this.fromTimestamp = fromTimestamp;
	}

	public Long getToTimestamp() {
		return toTimestamp;
	}

	public void setToTimestamp(Long toTimestamp) {
		this.toTimestamp = toTimestamp;
	}

	@Override
	public String toString() {
		return "RemoveByPeriod [timeline=" + timeline + ", fromTimestamp=" + fromTimestamp + ", toTimestamp="
				+ toTimestamp + "]";
	}
}