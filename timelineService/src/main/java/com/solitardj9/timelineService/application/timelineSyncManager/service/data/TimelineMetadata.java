package com.solitardj9.timelineService.application.timelineSyncManager.service.data;

import java.io.Serializable;
import java.sql.Timestamp;

public class TimelineMetadata implements Serializable {
	
	private static final long serialVersionUID = -1210536294753591921L;

	private String timeline;
	
	private Timestamp createdTime;
	
	public TimelineMetadata() {
		
	}

	public TimelineMetadata(String timeline, Timestamp createdTime) {
		this.timeline = timeline;
		this.createdTime = createdTime;
	}

	public String getTimeline() {
		return timeline;
	}

	public void setTimeline(String timeline) {
		this.timeline = timeline;
	}

	public Timestamp getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}

	@Override
	public String toString() {
		return "TimelineMetadata [timeline=" + timeline + ", createdTime=" + createdTime + "]";
	}
}