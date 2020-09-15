package com.solitardj9.timelineService.application.testManager.service.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.solitardj9.timelineService.application.messageFlowManager.model.PublishMessage;
import com.solitardj9.timelineService.application.testManager.service.TestManager;


@Service("testManager")
public class TestManagerImpl implements TestManager {
	
	@Autowired
	ApplicationEventPublisher applicationEventPublisher;

	@Override
	public void publishMessage(String message) {
		//
		PublishMessage publishMessage = new PublishMessage(UUID.randomUUID().toString(),"test", message);
		applicationEventPublisher.publishEvent(publishMessage);
	}
}