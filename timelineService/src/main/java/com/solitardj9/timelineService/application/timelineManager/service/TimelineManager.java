package com.solitardj9.timelineService.application.timelineManager.service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.solitardj9.timelineService.application.timelineManager.model.PutValue;
import com.solitardj9.timelineService.application.timelineManager.model.PutValues;
import com.solitardj9.timelineService.application.timelineManager.model.Remove;
import com.solitardj9.timelineService.application.timelineManager.model.RemoveByBefore;
import com.solitardj9.timelineService.application.timelineManager.model.RemoveByPeriod;
import com.solitardj9.timelineService.application.timelineManager.model.RemoveByTimes;
import com.solitardj9.timelineService.application.timelineManager.service.exception.ExceptionTimelineConflictFailure;
import com.solitardj9.timelineService.application.timelineManager.service.exception.ExceptionTimelineInternalFailure;
import com.solitardj9.timelineService.application.timelineManager.service.exception.ExceptionTimelineResourceNotFound;

public interface TimelineManager {
	
	/**
	 * @param timeline
	 * @return timeline if success. used for synchronization.
	 * @throws ExceptionTimelineConflictFailure
	 */
	public String addTimeline(String timeline) throws ExceptionTimelineConflictFailure;
	
	/**
	 * @param timeline
	 * @return timeline if success. used for synchronization.
	 * @throws ExceptionTimelineResourceNotFound
	 */
	public String deleteTimeline(String timeline) throws ExceptionTimelineResourceNotFound;
	
	public Set<String/*timeline*/> getkeySetOfTimelines();
	
	public TreeMap<Long/*Timestamp*/, String> get(String timeline) throws ExceptionTimelineResourceNotFound;
	
	public Set<Long/*Timestamp*/> getkeySetOfTimeline(String timeline) throws ExceptionTimelineResourceNotFound;
	
	public Map<String/*timeline*/, TreeMap<Long/*Timestamp*/, String>> getTimelines(List<String> timelines);
	
	public TreeMap<Long/*Timestamp*/, String> getByTime(String timeline, Long time) throws ExceptionTimelineResourceNotFound;
	
	public Map<String/*timeline*/, TreeMap<Long/*Timestamp*/, String>> getTimelinesByTime(List<String> timelines, Long time);
	
	public TreeMap<Long/*Timestamp*/, String> getByPreiod(String timeline, Long fromTime/*inclusive*/, Long toTime/*exclusive*/) throws ExceptionTimelineResourceNotFound;
	
	public Map<String/*timeline*/, TreeMap<Long/*Timestamp*/, String>> getTimelinesByPreiod(List<String> timelines, Long fromTime/*inclusive*/, Long toTime/*exclusive*/);
	
	/**
	 * @param timeline
	 * @param time
	 * @param value
	 * @return PutValue if success. used for synchronization.
	 * @throws ExceptionTimelineResourceNotFound
	 */
	public PutValue put(String timeline, Long time, String value) throws ExceptionTimelineResourceNotFound, ExceptionTimelineInternalFailure;
	
	/**
	 * @param timeline
	 * @param values
	 * @return PutValues if success. used for synchronization.
	 * @throws ExceptionTimelineResourceNotFound
	 */
	public PutValues putAll(String timeline, TreeMap<Long/*Timestamp*/, String/*value*/> values) throws ExceptionTimelineResourceNotFound, ExceptionTimelineInternalFailure;

	/**
	 * @param timeline
	 * @param time
	 * @param value
	 * @return PutValue if success. used for synchronization.
	 * @throws ExceptionTimelineResourceNotFound
	 */
	public PutValue update(String timeline, Long time, String value) throws ExceptionTimelineResourceNotFound, ExceptionTimelineInternalFailure;

	/**
	 * @param timeline
	 * @param values
	 * @return PutValues if success. used for synchronization.
	 * @throws ExceptionTimelineResourceNotFound
	 */
	public PutValues updateAll(String timeline, TreeMap<Long/*Timestamp*/, String/*value*/> values) throws ExceptionTimelineResourceNotFound, ExceptionTimelineInternalFailure;
	
	/**
	 * @param timeline
	 * @param time
	 * @return Remove if success. used for synchronization.
	 * @throws ExceptionTimelineResourceNotFound
	 * @throws ExceptionTimelineInternalFailure
	 */
	public Remove remove(String timeline, Long time) throws ExceptionTimelineResourceNotFound, ExceptionTimelineInternalFailure;
	
	/**
	 * @param timeline
	 * @param times
	 * @return RemoveByTime if success. used for synchronization.
	 * @throws ExceptionTimelineResourceNotFound
	 * @throws ExceptionTimelineInternalFailure
	 */
	public RemoveByTimes removeByTimes(String timeline, List<Long> times) throws ExceptionTimelineResourceNotFound, ExceptionTimelineInternalFailure;
	
	/**
	 * @param timeline
	 * @param fromTime
	 * @param toTime
	 * @return RemoveByPeriod if success. used for synchronization.
	 * @throws ExceptionTimelineResourceNotFound
	 */
	public RemoveByPeriod removeByPeriod(String timeline, Long fromTime/*inclusive*/, Long toTime/*exclusive*/) throws ExceptionTimelineResourceNotFound;
	
	/**
	 * @param timeline
	 * @param toTime
	 * @return RemoveByBefore if success. used for synchronization.
	 * @throws ExceptionTimelineResourceNotFound
	 */
	public RemoveByBefore removeByBefore(String timeline, Long toTime/*exclusive*/) throws ExceptionTimelineResourceNotFound;
	
	/**
	 * @param timeline
	 * @return timeline if success. used for synchronization.
	 * @throws ExceptionTimelineResourceNotFound
	 * @throws ExceptionTimelineInternalFailure
	 */
	public String clear(String timeline) throws ExceptionTimelineResourceNotFound, ExceptionTimelineInternalFailure;
	
	public Boolean isEmpty(String timeline) throws ExceptionTimelineResourceNotFound;
	
	public Integer size(String timeline) throws ExceptionTimelineResourceNotFound;
}