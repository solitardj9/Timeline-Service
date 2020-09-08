package com.solitardj9.timelineService.application.timelineManager.service.impl;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.solitardj9.timelineService.application.timelineManager.service.TimelineManager;

@Service("timelineManager")
public class TimelineManagerImpl implements TimelineManager {

	private Map<String, TreeMap<Integer, Object>> timelines = new ConcurrentHashMap<>();
	
	@Override
	public void addTimeline(String timelineName) {
		// TODO Auto-generated method stub
		if (timelines.containsKey(timelineName)) {
			return;
		}
		timelines.put(timelineName, new TreeMap<Integer, Object>());
	}

	@Override
	public TreeMap<Integer, Object> getTimeline(String timelineName) {
		// TODO Auto-generated method stub
		return new TreeMap<Integer, Object>(timelines.get(timelineName));
	}

	@Override
	public void put(String timelineName, Integer index, Object value) {
		// TODO Auto-generated method stub
		if (!timelines.containsKey(timelineName)) {
			return;
		}
		timelines.get(timelineName).put(index, value);
	}

	@Override
	public void remove(String timelineName, Integer index) {
		// TODO Auto-generated method stub
		if (!timelines.containsKey(timelineName)) {
			return;
		}
		timelines.get(timelineName).remove(index);
	}

}
