package com.solitardj9.timelineService.application.timelineSyncManager.service;

import java.util.List;
import java.util.TreeMap;

import com.solitardj9.timelineService.application.timelineManager.service.exception.ExceptionTimelineConflictFailure;
import com.solitardj9.timelineService.application.timelineManager.service.exception.ExceptionTimelineInternalFailure;
import com.solitardj9.timelineService.application.timelineManager.service.exception.ExceptionTimelineResourceNotFound;

public interface TimelineSyncManager {
	
	/**
	 * if success replicate added timeline.
	 * @param timeline
	 * @throws ExceptionTimelineConflictFailure
	 */
	public void addTimeline(String timeline) throws ExceptionTimelineConflictFailure;
	
	/**
	 * if success replicate deleted timeline.
	 * @param timeline
	 * @throws ExceptionTimelineResourceNotFound
	 */
	public void deleteTimeline(String timeline) throws ExceptionTimelineResourceNotFound;
	
	/**
	 * if success replicate putted value.
	 * @param timeline
	 * @param time
	 * @param value
	 * @throws ExceptionTimelineResourceNotFound
	 */
	public void put(String timeline, Long time, String value) throws ExceptionTimelineResourceNotFound, ExceptionTimelineInternalFailure;
	
	/**
	 * if success replicate putted values.
	 * @param timeline
	 * @param values
	 * @throws ExceptionTimelineResourceNotFound
	 */
	public void putAll(String timeline, TreeMap<Long/*Timestamp*/, String/*value*/> values) throws ExceptionTimelineResourceNotFound, ExceptionTimelineInternalFailure;
	
	/**
	 * if success replicate updated value.
	 * @param timeline
	 * @param time
	 * @param value
	 * @throws ExceptionTimelineResourceNotFound
	 * @throws ExceptionTimelineInternalFailure
	 */
	public void update(String timeline, Long time, String value) throws ExceptionTimelineResourceNotFound, ExceptionTimelineInternalFailure;
	
	/**
	 * if success replicate updated values.
	 * @param timeline
	 * @param values
	 * @throws ExceptionTimelineResourceNotFound
	 * @throws ExceptionTimelineInternalFailure
	 */
	public void updateAll(String timeline, TreeMap<Long/*Timestamp*/, String/*value*/> values) throws ExceptionTimelineResourceNotFound, ExceptionTimelineInternalFailure;
	
	/**
	 * if success replicate removed timestamp.
	 * @param timeline
	 * @param time
	 * @throws ExceptionTimelineResourceNotFound
	 * @throws ExceptionTimelineInternalFailure
	 */
	public void remove(String timeline, Long time) throws ExceptionTimelineResourceNotFound, ExceptionTimelineInternalFailure;
	
	/**
	 * if success replicate removed timestamps.
	 * @param timeline
	 * @param times
	 * @throws ExceptionTimelineResourceNotFound
	 * @throws ExceptionTimelineInternalFailure
	 */
	public void removeByTimes(String timeline, List<Long> times) throws ExceptionTimelineResourceNotFound, ExceptionTimelineInternalFailure;
	
	/**
	 * if success replicate removed period.
	 * @param timeline
	 * @param fromTime
	 * @param toTime
	 * @throws ExceptionTimelineResourceNotFound
	 */
	public void removeByPeriod(String timeline, Long fromTime/*inclusive*/, Long toTime/*exclusive*/) throws ExceptionTimelineResourceNotFound;
	
	/**
	 * if success replicate removed timestamp.
	 * @param timeline
	 * @param toTime
	 * @throws ExceptionTimelineResourceNotFound
	 */
	public void removeByBefore(String timeline, Long toTime/*exclusive*/) throws ExceptionTimelineResourceNotFound;
	
	/**
	 * if success replicate cleared timeline.
	 * @param timeline
	 * @throws ExceptionTimelineResourceNotFound
	 */
	public void clear(String timeline) throws ExceptionTimelineResourceNotFound, ExceptionTimelineInternalFailure;
}