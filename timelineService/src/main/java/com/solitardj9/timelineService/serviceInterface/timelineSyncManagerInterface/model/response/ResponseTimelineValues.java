package com.solitardj9.timelineService.serviceInterface.timelineSyncManagerInterface.model.response;

import java.util.TreeMap;

public class ResponseTimelineValues extends ResponseDefualt {
	//
	private TreeMap<Long, String> values;
	//private String values;
	
//	public ResponseTimelineValues() {
//		
//	}
//	
//	public ResponseTimelineValues(String values) {
//		this.values = values;
//	}
//
//	public ResponseTimelineValues(Integer status, String values) {
//		this.values = values;
//		setStatus(status);
//	}
//
//	public String getValues() {
//		return values;
//	}
//
//	public void setValues(String values) {
//		this.values = values;
//	}
//
//	@Override
//	public String toString() {
//		return "ResponseTimelineValues [values=" + values + ", toString()=" + super.toString() + "]";
//	}

	public ResponseTimelineValues(TreeMap<Long, String> values) {
		this.values = values;
	}
	
	public ResponseTimelineValues(Integer status, TreeMap<Long, String> values) {
		this.values = values;
		setStatus(status);
	}

	public TreeMap<Long, String> getValues() {
		return values;
	}

	public void setValues(TreeMap<Long, String> values) {
		this.values = values;
	}

	@Override
	public String toString() {
		return "ResponseTimelineValues [values=" + values + ", toString()=" + super.toString() + "]";
	}
}