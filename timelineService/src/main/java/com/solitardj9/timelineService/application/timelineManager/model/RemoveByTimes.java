package com.solitardj9.timelineService.application.timelineManager.model;

import java.util.List;

public class RemoveByTimes {
	//
	private String timeline;
	
	private List<Long> timestamps;
	
	public RemoveByTimes() {
		
	}

	public RemoveByTimes(String timeline, List<Long> timestamps) {
		this.timeline = timeline;
		this.timestamps = timestamps;
	}

	public String getTimeline() {
		return timeline;
	}

	public void setTimeline(String timeline) {
		this.timeline = timeline;
	}

	public List<Long> getTimestamps() {
		return timestamps;
	}

	public void setTimestamps(List<Long> timestamps) {
		this.timestamps = timestamps;
	}

	@Override
	public String toString() {
		return "RemoveByTime [timeline=" + timeline + ", timestamps=" + timestamps + "]";
	}
}