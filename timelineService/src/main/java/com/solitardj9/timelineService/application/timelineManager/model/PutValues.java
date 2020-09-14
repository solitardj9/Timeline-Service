package com.solitardj9.timelineService.application.timelineManager.model;

import java.util.TreeMap;

public class PutValues {
	//
	private String timeline;
	
	private TreeMap<Long/*timestamp*/, String/*value*/> values;
	
	public PutValues() {
		
	}

	public PutValues(String timeline, TreeMap<Long/*timestamp*/, String/*value*/> values) {
		this.timeline = timeline;
		this.values = values;
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
		return "PutValues [timeline=" + timeline + ", values=" + values + "]";
	}
}