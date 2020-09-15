package com.solitardj9.timelineService.application.timelineSyncManager.service.impl;

import java.util.List;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.solitardj9.timelineService.application.replicationManager.service.ReplicationManager;
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
import com.solitardj9.timelineService.application.timelineSyncManager.service.TimelineSyncManager;

@Service("timelineSyncManager")
public class TimelineSyncManagerImpl implements TimelineSyncManager {
	
	@Autowired
	TimelineManager timelineManager;
	
	@Autowired
	ReplicationManager replicationManager;
	
	@Override
	public void addTimeline(String timeline) throws ExceptionTimelineConflictFailure {
		//
		String addedTimeline = null;
		try {
			addedTimeline = timelineManager.addTimeline(timeline);
		} catch (ExceptionTimelineConflictFailure e) {
			throw new ExceptionTimelineConflictFailure();
		}
		
		if (addedTimeline != null) {
			replicationManager.replicateAddTimeline(addedTimeline);
		}
	}

	@Override
	public void deleteTimeline(String timeline) throws ExceptionTimelineResourceNotFound {
		//
		String deletedTimeline = null;
		try {
			deletedTimeline = timelineManager.deleteTimeline(timeline);
		} catch (ExceptionTimelineResourceNotFound e) {
			throw new ExceptionTimelineResourceNotFound();
		}
		
		if (deletedTimeline != null) {
			replicationManager.replicateDeleteTimeline(deletedTimeline);
		}
	}
	
	@Override
	public void put(String timeline, Long time, String value) throws ExceptionTimelineResourceNotFound, ExceptionTimelineInternalFailure {
		//
		PutValue puttedValue = null;
		try {
			puttedValue = timelineManager.put(timeline, time, value);
		} catch (ExceptionTimelineResourceNotFound e) {
			throw new ExceptionTimelineResourceNotFound();
		} catch (ExceptionTimelineInternalFailure e) {
			throw new ExceptionTimelineInternalFailure();
		}
		
		if (puttedValue != null) {
			replicationManager.replicatePut(puttedValue);
		}
	}

	@Override
	public void putAll(String timeline, TreeMap<Long, String> values) throws ExceptionTimelineResourceNotFound, ExceptionTimelineInternalFailure {
		//
		PutValues puttedValues = null;
		try {
			puttedValues = timelineManager.putAll(timeline, values);
		} catch (ExceptionTimelineResourceNotFound e) {
			throw new ExceptionTimelineResourceNotFound();
		} catch (ExceptionTimelineInternalFailure e) {
			throw new ExceptionTimelineInternalFailure();
		}
		
		if (puttedValues != null) {
			replicationManager.replicatePutAll(puttedValues);
		}
	}

	@Override
	public void update(String timeline, Long time, String value) throws ExceptionTimelineResourceNotFound, ExceptionTimelineInternalFailure {
		//
		PutValue updatedValue = null;
		try {
			updatedValue = timelineManager.update(timeline, time, value);
		} catch (ExceptionTimelineResourceNotFound e) {
			throw new ExceptionTimelineResourceNotFound();
		} catch (ExceptionTimelineInternalFailure e) {
			throw new ExceptionTimelineInternalFailure();
		}
		
		if (updatedValue != null) {
			replicationManager.replicateUpdate(updatedValue);
		}
	}

	@Override
	public void updateAll(String timeline, TreeMap<Long, String> values) throws ExceptionTimelineResourceNotFound, ExceptionTimelineInternalFailure {
		//
		PutValues updatedValues = null;
		try {
			updatedValues = timelineManager.updateAll(timeline, values);
		} catch (ExceptionTimelineResourceNotFound e) {
			throw new ExceptionTimelineResourceNotFound();
		} catch (ExceptionTimelineInternalFailure e) {
			throw new ExceptionTimelineInternalFailure();
		}
		
		if (updatedValues != null) {
			replicationManager.replicateUpdateAll(updatedValues);
		}
	}

	@Override
	public void remove(String timeline, Long time) throws ExceptionTimelineResourceNotFound, ExceptionTimelineInternalFailure {
		//
		Remove removed = null;
		try {
			removed = timelineManager.remove(timeline, time);
		} catch (ExceptionTimelineResourceNotFound e) {
			throw new ExceptionTimelineResourceNotFound();
		} catch (ExceptionTimelineInternalFailure e) {
			throw new ExceptionTimelineInternalFailure();
		}
		
		if (removed != null) {
			replicationManager.replicateRemove(removed);
		}
	}

	@Override
	public void removeByTimes(String timeline, List<Long> times) throws ExceptionTimelineResourceNotFound, ExceptionTimelineInternalFailure {
		//
		RemoveByTimes removedByTimes = null;
		try {
			removedByTimes = timelineManager.removeByTimes(timeline, times);
		} catch (ExceptionTimelineResourceNotFound e) {
			throw new ExceptionTimelineResourceNotFound();
		} catch (ExceptionTimelineInternalFailure e) {
			throw new ExceptionTimelineInternalFailure();
		}
		
		if (removedByTimes != null) {
			replicationManager.replicateRemoveByTimes(removedByTimes);
		}
	}

	@Override
	public void removeByPeriod(String timeline, Long fromTime, Long toTime) throws ExceptionTimelineResourceNotFound {
		//
		RemoveByPeriod removedByPeriod = null;
		try {
			removedByPeriod = timelineManager.removeByPeriod(timeline, fromTime, toTime);
		} catch (ExceptionTimelineResourceNotFound e) {
			throw new ExceptionTimelineResourceNotFound();
		}
		
		if (removedByPeriod != null) {
			replicationManager.replicateRemoveByPeriod(removedByPeriod);
		}
	}

	@Override
	public void removeByBefore(String timeline, Long toTime) throws ExceptionTimelineResourceNotFound {
		//
		RemoveByBefore removedByBefore = null;
		try {
			removedByBefore = timelineManager.removeByBefore(timeline, toTime);
		} catch (ExceptionTimelineResourceNotFound e) {
			throw new ExceptionTimelineResourceNotFound();
		}
		
		if (removedByBefore != null) {
			replicationManager.replicateRemoveByBefore(removedByBefore);
		}
	}

	@Override
	public void clear(String timeline) throws ExceptionTimelineResourceNotFound, ExceptionTimelineInternalFailure {
		//
		String clearedTmeline = null;
		try {
			clearedTmeline = timelineManager.clear(timeline);
		} catch (ExceptionTimelineResourceNotFound e) {
			throw new ExceptionTimelineResourceNotFound();
		} catch (ExceptionTimelineInternalFailure e) {
			throw new ExceptionTimelineInternalFailure();
		}
		
		if (clearedTmeline != null) {
			replicationManager.replicateClear(clearedTmeline);
		}
	}
}