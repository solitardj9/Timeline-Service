package com.solitardj9.timelineService.application.timelineManager.service.impl;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.solitardj9.timelineService.application.timelineManager.service.TimelineManager;
import com.solitardj9.timelineService.application.timelineManager.service.exception.ExceptionTimelineConflictFailure;
import com.solitardj9.timelineService.application.timelineManager.service.exception.ExceptionTimelineResourceNotFound;

@Service("timelineManager")
public class TimelineManagerImpl implements TimelineManager {

	private Map<String, TreeMap<Integer, Object>> timelines = new ConcurrentHashMap<>();
	
	@Override
	public void addTimeline(String timeline) throws ExceptionTimelineConflictFailure {
		//
		if (timelines.containsKey(timeline)) {
			throw new ExceptionTimelineConflictFailure();
		}
		timelines.put(timeline, new TreeMap<Integer, Object>());
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, TreeMap<Long, String>> getTimelines(List<String> timelines) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TreeMap<Long, String> getTimelineByTime(String timeline, Long time)
			throws ExceptionTimelineResourceNotFound {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, TreeMap<Long, String>> getTimelinesByTime(List<String> timelines, Long time) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TreeMap<Long, String> getTimelineByPreiod(String timeline, Long fromTime, Long toTime)
			throws ExceptionTimelineResourceNotFound {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, TreeMap<Long, String>> getTimelinesByPreiod(List<String> timelines, Long fromTime, Long toTime) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void put(String timeline, Long time, String value) throws ExceptionTimelineResourceNotFound {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void putAll(String timeline, TreeMap<Long, String> values) throws ExceptionTimelineResourceNotFound {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(String timeline, Long time, String value) throws ExceptionTimelineResourceNotFound {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateAll(String timeline, TreeMap<Long, String> values) throws ExceptionTimelineResourceNotFound {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void remove(String timeline, Long time) throws ExceptionTimelineResourceNotFound {
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		return null;
	}

	
	

//
//	@Override
//	public TreeMap<Integer, Object> getTimeline(String timeline) {
//		// TODO Auto-generated method stub
//		return new TreeMap<Integer, Object>(timelines.get(timeline));
//	}
//
//	@Override
//	public void put(String timeline, Integer index, Object value) {
//		// TODO Auto-generated method stub
//		if (!timelines.containsKey(timeline)) {
//			return;
//		}
//		timelines.get(timeline).put(index, value);
//	}
//
//	@Override
//	public void remove(String timeline, Integer index) {
//		// TODO Auto-generated method stub
//		if (!timelines.containsKey(timeline)) {
//			return;
//		}
//		timelines.get(timeline).remove(index);
//	}

}
