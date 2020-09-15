package com.solitardj9.timelineService.application.replicationManager.service;

import com.solitardj9.timelineService.application.timelineManager.model.PutValue;
import com.solitardj9.timelineService.application.timelineManager.model.PutValues;
import com.solitardj9.timelineService.application.timelineManager.model.Remove;
import com.solitardj9.timelineService.application.timelineManager.model.RemoveByBefore;
import com.solitardj9.timelineService.application.timelineManager.model.RemoveByPeriod;
import com.solitardj9.timelineService.application.timelineManager.model.RemoveByTimes;

public interface ReplicationManager {
	//
	public void replicateAddTimeline(String timeline);
	
	public void replicateDeleteTimeline(String timeline);
	
	public void replicatePut(PutValue putValue);
	
	public void replicatePutAll(PutValues putValues);
	
	public void replicateUpdate(PutValue putValue);
	
	public void replicateUpdateAll(PutValues putValues);
	
	public void replicateRemove(Remove remove);
	
	public void replicateRemoveByTimes(RemoveByTimes removeByTimes);
	
	public void replicateRemoveByPeriod(RemoveByPeriod removeByPeriod);
	
	public void replicateRemoveByBefore(RemoveByBefore removeByBefore);
	
	public void replicateClear(String timeline);
}