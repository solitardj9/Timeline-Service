package com.solitardj9.timelineService.application.timelineSyncManager.service;

import com.solitardj9.timelineService.application.messageFlowManager.model.ConsumeMessage;

public interface TimelineRestoreManager {
	
	public void testRestore();
	
	public void executeRestore();
	
	public void addReplicationEvent(ConsumeMessage consumeMessage);
	
	public void restoreReplicationEvent();
}