package com.solitardj9.timelineService.application.syncManager.service.impl;

import org.springframework.context.event.EventListener;

import com.solitardj9.timelineService.application.messageFlowManager.model.ConsumeMessage;
import com.solitardj9.timelineService.application.syncManager.service.SyncManager;

public class SyncManagerImpl implements SyncManager {
	
	@EventListener(classes=ConsumeMessage.class, condition = "#consumeMessage.type == 'replicate'")
	public void onConsumeMessageForReplicate(ConsumeMessage consumeMessage) {
		
	}
	
	@EventListener(classes=ConsumeMessage.class, condition = "#consumeMessage.type == 'delete'")
	public void onConsumeMessageForDelete(ConsumeMessage consumeMessage) {
		
	}
}