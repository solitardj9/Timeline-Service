package com.solitardj9.timelineService.serviceInterface.timelineSyncManagerInterface.model;

import java.util.TreeMap;

public class RequestPutAll extends RequestPutDefault {
	//
	private TreeMap<Long, String> values;
	
	public RequestPutAll() {
		
	}

	public RequestPutAll(TreeMap<Long, String> values) {
		this.values = values;
	}

	public TreeMap<Long, String> getValues() {
		return values;
	}

	public void setValues(TreeMap<Long, String> values) {
		this.values = values;
	}

	@Override
	public String toString() {
		return "RequestPutAll [values=" + values + "]";
	}
}