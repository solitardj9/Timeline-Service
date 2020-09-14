package com.solitardj9.timelineService.application.timelineManager.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.solitardj9.timelineService.application.timelineManager.model.PutValue;
import com.solitardj9.timelineService.application.timelineManager.model.PutValues;
import com.solitardj9.timelineService.application.timelineManager.model.Remove;
import com.solitardj9.timelineService.application.timelineManager.model.RemoveByBefore;
import com.solitardj9.timelineService.application.timelineManager.model.RemoveByPeriod;
import com.solitardj9.timelineService.application.timelineManager.model.RemoveByTimes;
import com.solitardj9.timelineService.application.timelineManager.service.TimelineManager;
import com.solitardj9.timelineService.application.timelineManager.service.exception.ExceptionTimelineConflictFailure;
import com.solitardj9.timelineService.application.timelineManager.service.exception.ExceptionTimelineInternalFailure;
import com.solitardj9.timelineService.application.timelineManager.service.exception.ExceptionTimelineResourceNotFound;
import com.solitardj9.timelineService.utils.jsonUtil.JsonUtil;

@Service("timelineManager")
public class TimelineManagerImpl implements TimelineManager {

	private static final Logger logger = LoggerFactory.getLogger(TimelineManagerImpl.class);
	
	private Map<String, ConcurrentNavigableMap<Long, String>> timelines = new ConcurrentHashMap<>();
	
	@Override
	public String addTimeline(String timeline) throws ExceptionTimelineConflictFailure {
		//
		if (timelines.containsKey(timeline)) {
			throw new ExceptionTimelineConflictFailure();
		}
		timelines.put(timeline, new ConcurrentSkipListMap<Long, String>());
		return timeline;
	}

	@Override
	public String deleteTimeline(String timeline) throws ExceptionTimelineResourceNotFound {
		//
		if (!timelines.containsKey(timeline)) {
			throw new ExceptionTimelineResourceNotFound();
		}
		timelines.remove(timeline);
		return timeline;
	}
	
	@Override
	public Set<String> getkeySetOfTimelines() {
		//
		return new HashSet<String>(timelines.keySet());
	}
	
	@Override
	public TreeMap<Long, String> getTimeline(String timeline) throws ExceptionTimelineResourceNotFound {
		//
		if (!timelines.containsKey(timeline)) {
			throw new ExceptionTimelineResourceNotFound();
		}
		return new TreeMap<Long, String>(timelines.get(timeline));
	}
	
	@Override
	public Set<Long/*Timestamp*/> getkeySetOfTimeline(String timeline) throws ExceptionTimelineResourceNotFound {
		//
		if (!timelines.containsKey(timeline)) {
			throw new ExceptionTimelineResourceNotFound();
		}
		return new HashSet<Long>(timelines.get(timeline).keySet());
	}

	/**
	 * Timeline which is not exist, will not be inclued in result.  
	 */
	@Override
	public Map<String, TreeMap<Long, String>> getTimelines(List<String> timelines) {
		//
		Map<String, TreeMap<Long, String>> retMap = new HashMap<>();
		for (String timeline : timelines) {
			try {
				retMap.put(timeline, getTimeline(timeline));
			} catch (ExceptionTimelineResourceNotFound e) {
				//e.printStackTrace();
				logger.error("[TimelineManager].getTimelines : error = " + e.toString());
			}
		}
		
		return retMap;
	}

	@Override
	public TreeMap<Long, String> getTimelineByTime(String timeline, Long time) throws ExceptionTimelineResourceNotFound {
		//
		TreeMap<Long, String> retMap = new TreeMap<>();
		
		if (!timelines.containsKey(timeline)) {
			throw new ExceptionTimelineResourceNotFound();
		}
		
		retMap.put(time, timelines.get(timeline).get(time));
		return retMap;
	}

	@Override
	public Map<String, TreeMap<Long, String>> getTimelinesByTime(List<String> timelines, Long time) {
		//
		Map<String, TreeMap<Long, String>> retMap = new HashMap<>();
		for (String timeline : timelines) {
			try {
				retMap.put(timeline, getTimelineByTime(timeline, time));
			} catch (ExceptionTimelineResourceNotFound e) {
				//e.printStackTrace();
				logger.error("[TimelineManager].getTimelinesByTime : error = " + e.toString());
			}
		}
		
		return retMap;
	}

	@Override
	public TreeMap<Long, String> getTimelineByPreiod(String timeline, Long fromTime, Long toTime) throws ExceptionTimelineResourceNotFound {
		//
		TreeMap<Long, String> retMap = new TreeMap<>();
		
		if (!timelines.containsKey(timeline)) {
			throw new ExceptionTimelineResourceNotFound();
		}

		retMap = new TreeMap<>(timelines.get(timeline).subMap(fromTime, toTime));
		return retMap;
	}

	@Override
	public Map<String, TreeMap<Long, String>> getTimelinesByPreiod(List<String> timelines, Long fromTime, Long toTime) {
		//
		Map<String, TreeMap<Long, String>> retMap = new HashMap<>();
		for (String timeline : timelines) {
			try {
				retMap.put(timeline, getTimelineByPreiod(timeline, fromTime, toTime));
			} catch (ExceptionTimelineResourceNotFound e) {
				//e.printStackTrace();
				logger.error("[TimelineManager].getTimelinesByPreiod : error = " + e.toString());
			}
		}
		
		return retMap;
	}
	
	@Override
	public PutValue put(String timeline, Long time, String value) throws ExceptionTimelineResourceNotFound, ExceptionTimelineInternalFailure {
		//
		if (!timelines.containsKey(timeline)) {
			throw new ExceptionTimelineResourceNotFound();
		}
		
		try {
			timelines.get(timeline).put(time, value);
			return new PutValue(timeline, time, value);
		} catch(Exception e) {
			logger.error("[TimelineManager].put : error = " + e.toString());
			throw new ExceptionTimelineInternalFailure();
		}
	}

	@Override
	public PutValues putAll(String timeline, TreeMap<Long, String> values) throws ExceptionTimelineResourceNotFound, ExceptionTimelineInternalFailure {
		//
		if (!timelines.containsKey(timeline)) {
			throw new ExceptionTimelineResourceNotFound();
		}
		
		try {
			timelines.get(timeline).putAll(values);
			return new PutValues(timeline, values);
		} catch(Exception e) {
			logger.error("[TimelineManager].putAll : error = " + e.toString());
			throw new ExceptionTimelineInternalFailure();
		}
	}

	@Override
	public PutValue update(String timeline, Long time, String value) throws ExceptionTimelineResourceNotFound, ExceptionTimelineInternalFailure {
		//
		if (!timelines.containsKey(timeline)) {
			throw new ExceptionTimelineResourceNotFound();
		}
		
		String prevValue = timelines.get(timeline).get(time);
		if (prevValue == null || prevValue.isEmpty()) {
			//
			try {
				timelines.get(timeline).put(time, value);
				return new PutValue(timeline, time, value);
			} catch(Exception e) {
				logger.error("[TimelineManager].update : error = " + e.toString());
				throw new ExceptionTimelineInternalFailure();
			}
		}
		else {
			//
			try {
				String mergedJsonString = JsonUtil.mergeJsonString(prevValue, value);
				timelines.get(timeline).put(time, mergedJsonString);
				return new PutValue(timeline, time, mergedJsonString);
			} catch(Exception e) {
				logger.error("[TimelineManager].update : error = " + e.toString());
				throw new ExceptionTimelineInternalFailure();
			}
		}
	}

	@Override
	public PutValues updateAll(String timeline, TreeMap<Long, String> values) throws ExceptionTimelineResourceNotFound, ExceptionTimelineInternalFailure {
		//
		if (!timelines.containsKey(timeline)) {
			throw new ExceptionTimelineResourceNotFound();
		}
		
		TreeMap<Long, String> retValues = new TreeMap<>();
		for (Entry<Long, String> iter : values.entrySet()) {
			try {
				String prevValue = timelines.get(timeline).get(iter.getKey());
				if (prevValue == null || prevValue.isEmpty()) {
					//
					try {
						timelines.get(timeline).put(iter.getKey(), iter.getValue());
						retValues.put(iter.getKey(), iter.getValue());
					} catch(Exception e) {
						logger.error("[TimelineManager].updateAll : error = " + e.toString());
					}
				}
				else {
					//
					try {
						String mergedJsonString = JsonUtil.mergeJsonString(prevValue, iter.getValue());
						timelines.get(timeline).put(iter.getKey(), mergedJsonString);
						retValues.put(iter.getKey(), mergedJsonString);
					} catch(Exception e) {
						logger.error("[TimelineManager].updateAll : error = " + e.toString());
					}
				}
			} catch (Exception e) {
				//e.printStackTrace();
				logger.error("[TimelineManager].updateAll : error = " + e.toString());
			}
		}
		
		if (retValues.isEmpty()) {
			logger.error("[TimelineManager].updateAll : nothing to be updated.");
			throw new ExceptionTimelineInternalFailure(); 
		}
		
		return new PutValues(timeline, retValues);
	}

	@Override
	public Remove remove(String timeline, Long time) throws ExceptionTimelineResourceNotFound, ExceptionTimelineInternalFailure {
		//
		if (!timelines.containsKey(timeline)) {
			throw new ExceptionTimelineResourceNotFound();
		}
		
		try {
			timelines.get(timeline).remove(time);
			return new Remove(timeline, time);
		} catch(Exception e) {
			throw new ExceptionTimelineInternalFailure();
		}
	}

	@Override
	public RemoveByTimes removeByTimes(String timeline, List<Long> times) throws ExceptionTimelineResourceNotFound, ExceptionTimelineInternalFailure {
		//
		if (!timelines.containsKey(timeline)) {
			throw new ExceptionTimelineResourceNotFound();
		}
		
		List<Long> retValues = new ArrayList<>();
		for (Long time : times) {
			try {
				timelines.get(timeline).remove(time);
				retValues.add(time);
			} catch(Exception e) {
				logger.error("[TimelineManager].removeByTimes : error = " + e.toString());
			}
		}
		
		if (retValues.isEmpty()) {
			logger.error("[TimelineManager].removeByTimes : nothing to be deleted.");
			throw new ExceptionTimelineInternalFailure(); 
		}
		
		return new RemoveByTimes(timeline, retValues);
	}

	@Override
	public RemoveByPeriod removeByPeriod(String timeline, Long fromTime, Long toTime) throws ExceptionTimelineResourceNotFound {
		//
		if (!timelines.containsKey(timeline)) {
			throw new ExceptionTimelineResourceNotFound();
		}

		Set<Long> keys = timelines.get(timeline).subMap(fromTime, toTime).keySet();
		if (keys != null) {
			Set<Long> tmpKeys = new HashSet<Long>(keys);
			for (Long tmpKey : tmpKeys) {
				timelines.get(timeline).remove(tmpKey);
			}
		}
		
		return new RemoveByPeriod(timeline, fromTime, toTime);
	}

	@Override
	public RemoveByBefore removeByBefore(String timeline, Long toTime) throws ExceptionTimelineResourceNotFound {
		//
		if (!timelines.containsKey(timeline)) {
			throw new ExceptionTimelineResourceNotFound();
		}

		Set<Long> keys = timelines.get(timeline).headMap(toTime).keySet();
		if (keys != null) {
			Set<Long> tmpKeys = new HashSet<Long>(keys);
			for (Long tmpKey : tmpKeys) {
				timelines.get(timeline).remove(tmpKey);
			}
		}
		
		return new RemoveByBefore(timeline, toTime);
	}

	@Override
	public String clear(String timeline) throws ExceptionTimelineResourceNotFound, ExceptionTimelineInternalFailure {
		//
		if (!timelines.containsKey(timeline)) {
			throw new ExceptionTimelineResourceNotFound();
		}
		
		try {
			timelines.get(timeline).clear();
			return timeline;
		} catch(Exception e) {
			throw new ExceptionTimelineInternalFailure();
		}
	}

	@Override
	public Boolean isEmpty(String timeline) throws ExceptionTimelineResourceNotFound {
		//
		if (!timelines.containsKey(timeline)) {
			throw new ExceptionTimelineResourceNotFound();
		}
		return timelines.get(timeline).isEmpty();
	}
	
	@Override
	public Integer size(String timeline) throws ExceptionTimelineResourceNotFound {
		//
		if (!timelines.containsKey(timeline)) {
			throw new ExceptionTimelineResourceNotFound();
		}
		return timelines.get(timeline).size();
	}
}