package com.solitardj9.timelineService.serviceInterface.timelineSyncManagerInterface.model.response;

import java.util.Map;
import java.util.TreeMap;

public class ResponseTimelinesValues extends ResponseDefualt {
	//
	private Map<String, TreeMap<Long, String>> values;
	
	public ResponseTimelinesValues() {
		
	}

	public ResponseTimelinesValues(Map<String, TreeMap<Long, String>>values) {
		this.values = values;
	}
	
	public ResponseTimelinesValues(Integer status, Map<String, TreeMap<Long, String>> values) {
		this.values = values;
		setStatus(status);
	}

	public Map<String, TreeMap<Long, String>> getValues() {
		return values;
	}

	public void setValues(Map<String, TreeMap<Long, String>>values) {
		this.values = values;
	}

	@Override
	public String toString() {
		return "ResponseTimelineValues [values=" + values + ", toString()=" + super.toString() + "]";
	}
	
}