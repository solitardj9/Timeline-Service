package com.solitardj9.timelineService.application.timelineManager.service;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.solitardj9.timelineService.application.timelineManager.service.exception.ExceptionTimelineConflictFailure;
import com.solitardj9.timelineService.application.timelineManager.service.exception.ExceptionTimelineResourceNotFound;

public interface TimelineManager {
	//
	public void addTimeline(String timeline) throws ExceptionTimelineConflictFailure;
	
	public void deleteTimeline(String timeline) throws ExceptionTimelineResourceNotFound;
	
	public TreeMap<Long/*Timestamp*/, String> getTimeline(String timeline) throws ExceptionTimelineResourceNotFound;
	
	public Map<String/*timeline*/, TreeMap<Long/*Timestamp*/, String>> getTimelines(List<String> timelines);
	
	public TreeMap<Long/*Timestamp*/, String> getTimelineByTime(String timeline, Long time) throws ExceptionTimelineResourceNotFound;
	
	public Map<String/*timeline*/, TreeMap<Long/*Timestamp*/, String>> getTimelinesByTime(List<String> timelines, Long time);
	
	public TreeMap<Long/*Timestamp*/, String> getTimelineByPreiod(String timeline, Long fromTime/*inclusive*/, Long toTime/*exclusive*/) throws ExceptionTimelineResourceNotFound;
	
	public Map<String/*timeline*/, TreeMap<Long/*Timestamp*/, String>> getTimelinesByPreiod(List<String> timelines, Long fromTime/*inclusive*/, Long toTime/*exclusive*/);
	
	public void put(String timeline, Long time, String value) throws ExceptionTimelineResourceNotFound;
	
	public void putAll(String timeline, TreeMap<Long/*Timestamp*/, String/*value*/> values) throws ExceptionTimelineResourceNotFound;
	
	public void update(String timeline, Long time, String value) throws ExceptionTimelineResourceNotFound;
	
	public void updateAll(String timeline, TreeMap<Long/*Timestamp*/, String/*value*/> values) throws ExceptionTimelineResourceNotFound;
	
	public String remove(String timeline, Long time) throws ExceptionTimelineResourceNotFound;
	
	public void removeByTimes(String timeline, List<Long> times) throws ExceptionTimelineResourceNotFound;
	
	public void removeByPeriod(String timeline, Long fromTime/*inclusive*/, Long toTime/*exclusive*/) throws ExceptionTimelineResourceNotFound;
	
	public void removeByBefore(String timeline, Long toTime/*exclusive*/) throws ExceptionTimelineResourceNotFound;
	
	public void clear(String timeline) throws ExceptionTimelineResourceNotFound;
	
	public Boolean isEmpty(String timeline) throws ExceptionTimelineResourceNotFound;
	
	public Integer size(String timeline) throws ExceptionTimelineResourceNotFound;
}