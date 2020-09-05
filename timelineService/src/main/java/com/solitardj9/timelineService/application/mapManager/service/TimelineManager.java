package com.solitardj9.timelineService.application.mapManager.service;

import java.util.TreeMap;

public interface TimelineManager {
	//
	public void addTimeline(String timelineName);
	
	public TreeMap<Integer, Object> getTimeline(String timelineName);
	
	public void put(String timelineName, Integer index, Object value);
	
	public void remove(String timelineName, Integer index);
}