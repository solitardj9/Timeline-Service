package com.solitardj9.timelineService.application.timelineManager.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.solitardj9.timelineService.application.timelineManager.service.TimelineManager;
import com.solitardj9.timelineService.application.timelineManager.service.exception.ExceptionTimelineConflictFailure;
import com.solitardj9.timelineService.application.timelineManager.service.exception.ExceptionTimelineResourceNotFound;
import com.solitardj9.timelineService.utils.jsonUtil.JsonUtil;

@Service("timelineManager")
public class TimelineManagerImpl implements TimelineManager {

	private static final Logger logger = LoggerFactory.getLogger(TimelineManagerImpl.class);
	
	private Map<String, TreeMap<Long, String>> timelines = new ConcurrentHashMap<>();
	
	@Override
	public void addTimeline(String timeline) throws ExceptionTimelineConflictFailure {
		//
		if (timelines.containsKey(timeline)) {
			throw new ExceptionTimelineConflictFailure();
		}
		timelines.put(timeline, new TreeMap<Long, String>());
	}

	@Override
	public void deleteTimeline(String timeline) throws ExceptionTimelineResourceNotFound {
		//
		if (!timelines.containsKey(timeline)) {
			throw new ExceptionTimelineResourceNotFound();
		}
		timelines.remove(timeline);
	}

	
	@Override
	public TreeMap<Long, String> getTimeline(String timeline) throws ExceptionTimelineResourceNotFound {
		//
		if (!timelines.containsKey(timeline)) {
			throw new ExceptionTimelineResourceNotFound();
		}
		return new TreeMap<Long, String>(timelines.get(timeline));
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
	public void put(String timeline, Long time, String value) throws ExceptionTimelineResourceNotFound {
		//
		if (!timelines.containsKey(timeline)) {
			throw new ExceptionTimelineResourceNotFound();
		}
		timelines.get(timeline).put(time, value);
	}

	@Override
	public void putAll(String timeline, TreeMap<Long, String> values) throws ExceptionTimelineResourceNotFound {
		//
		if (!timelines.containsKey(timeline)) {
			throw new ExceptionTimelineResourceNotFound();
		}
		timelines.get(timeline).putAll(values);
	}

	@Override
	public void update(String timeline, Long time, String value) throws ExceptionTimelineResourceNotFound {
		//
		if (!timelines.containsKey(timeline)) {
			throw new ExceptionTimelineResourceNotFound();
		}
		String prevValue = timelines.get(timeline).get(time);
		if (prevValue == null || prevValue.isEmpty()) {
			timelines.get(timeline).put(time, value);
		}
		else {
			String mergedJsonString = JsonUtil.mergeJsonString(prevValue, value);
			timelines.get(timeline).put(time, mergedJsonString);
		}
	}

	@Override
	public void updateAll(String timeline, TreeMap<Long, String> values) throws ExceptionTimelineResourceNotFound {
		//
		if (!timelines.containsKey(timeline)) {
			throw new ExceptionTimelineResourceNotFound();
		}
		
		for (Entry<Long, String> iter : values.entrySet()) {
			try {
				update(timeline, iter.getKey(), iter.getValue());
			} catch (Exception e) {
				//e.printStackTrace();
				logger.error("[TimelineManager].updateAll : error = " + e.toString());
			}
		}
	}

	@Override
	public String remove(String timeline, Long time) throws ExceptionTimelineResourceNotFound {
		//
		if (!timelines.containsKey(timeline)) {
			throw new ExceptionTimelineResourceNotFound();
		}
		return timelines.get(timeline).remove(time);
	}

	@Override
	public void removeByTimes(String timeline, List<Long> times) throws ExceptionTimelineResourceNotFound {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeByPeriod(String timeline, Long fromTime, Long toTime) throws ExceptionTimelineResourceNotFound {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeByBefore(String timeline, Long toTime) throws ExceptionTimelineResourceNotFound {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clear(String timeline) throws ExceptionTimelineResourceNotFound {
		// TODO Auto-generated method stub
		
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