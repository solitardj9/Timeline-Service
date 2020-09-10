package com.solitardj9.timelineService.application.replicationManager.service.impl;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import com.solitardj9.timelineService.application.messageFlowManager.model.ConsumeMessage;
import com.solitardj9.timelineService.application.replicationManager.service.ReplicationManager;

@Service("replicationManager")
public class ReplicationManagerImpl implements ReplicationManager {
	
	@EventListener(classes=ConsumeMessage.class, condition = "#consumeMessage.type == 'addTimeline'")
	public void onConsumeMessageForReplicate(ConsumeMessage consumeMessage) {
		
	}
	
	@EventListener(classes=ConsumeMessage.class, condition = "#consumeMessage.type == 'deleteTimeline'")
	public void onConsumeMessageForDelete(ConsumeMessage consumeMessage) {
		
	}
	
	@EventListener(classes=ConsumeMessage.class, condition = "#consumeMessage.type == 'put'")
	public void onConsumeMessageForPut(ConsumeMessage consumeMessage) {
		
	}
	
	@EventListener(classes=ConsumeMessage.class, condition = "#consumeMessage.type == 'putAll'")
	public void onConsumeMessageForPutAll(ConsumeMessage consumeMessage) {
		
	}
	
	@EventListener(classes=ConsumeMessage.class, condition = "#consumeMessage.type == 'update'")
	public void onConsumeMessageForUpdate(ConsumeMessage consumeMessage) {
		
	}
	
	@EventListener(classes=ConsumeMessage.class, condition = "#consumeMessage.type == 'updateAll'")
	public void onConsumeMessageForUpdateAll(ConsumeMessage consumeMessage) {
		
	}
	
	@EventListener(classes=ConsumeMessage.class, condition = "#consumeMessage.type == 'remove'")
	public void onConsumeMessageForRemove(ConsumeMessage consumeMessage) {
		
	}
	
	@EventListener(classes=ConsumeMessage.class, condition = "#consumeMessage.type == 'removeByTimes'")
	public void onConsumeMessageForRemoveByTimes(ConsumeMessage consumeMessage) {
		
	}
	
	@EventListener(classes=ConsumeMessage.class, condition = "#consumeMessage.type == 'removeByPeriod'")
	public void onConsumeMessageForRemoveByPeriod(ConsumeMessage consumeMessage) {
		
	}
	
	@EventListener(classes=ConsumeMessage.class, condition = "#consumeMessage.type == 'removeByBefore'")
	public void onConsumeMessageForRemoveByBefore(ConsumeMessage consumeMessage) {
		
	}
	
	@EventListener(classes=ConsumeMessage.class, condition = "#consumeMessage.type == 'clear'")
	public void onConsumeMessageForClear(ConsumeMessage consumeMessage) {
		
	}
}