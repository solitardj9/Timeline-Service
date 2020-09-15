package com.solitardj9.timelineService.serviceInterface.timelineSyncManagerInterface.model;

import java.util.TreeMap;

public class ResultTimelineValues extends ResultDefualt {
	//
	private String timeline;
	
	private TreeMap<Long, String> values;
	
	public ResultTimelineValues() {
		
	}

	public ResultTimelineValues(String timeline, TreeMap<Long, String> values) {
		this.timeline = timeline;
		this.values = values;
	}
	
	public ResultTimelineValues(Integer status, String timeline, TreeMap<Long, String> values) {
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
		return "ResultTimelineValues [timeline=" + timeline + ", values=" + values + ", toString()=" + super.toString()
				+ "]";
	}
}