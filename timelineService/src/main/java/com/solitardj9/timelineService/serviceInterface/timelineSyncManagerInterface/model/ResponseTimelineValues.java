package com.solitardj9.timelineService.serviceInterface.timelineSyncManagerInterface.model;

import java.util.TreeMap;

public class ResponseTimelineValues extends ResponseDefualt {
	//
	private String timeline;
	
	private TreeMap<Long, String> values;
	
	public ResponseTimelineValues() {
		
	}

	public ResponseTimelineValues(String timeline, TreeMap<Long, String> values) {
		this.timeline = timeline;
		this.values = values;
	}
	
	public ResponseTimelineValues(Integer status, String timeline, TreeMap<Long, String> values) {
		this.timeline = timeline;
		this.values = values;
		setStatus(status);
	}

	public String getTimeline() {
		return timeline;
	}

	public void setTimeline(String timeline) {
		this.timeline = timeline;
	}

	public TreeMap<Long, String> getValues() {
		return values;
	}

	public void setValues(TreeMap<Long, String> values) {
		this.values = values;
	}

	@Override
	public String toString() {
		return "ResponseTimelineValues [timeline=" + timeline + ", values=" + values + ", toString()=" + super.toString() + "]";
	}
}