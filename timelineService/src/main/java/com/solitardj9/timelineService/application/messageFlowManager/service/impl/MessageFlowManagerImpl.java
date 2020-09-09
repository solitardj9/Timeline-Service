package com.solitardj9.timelineService.application.messageFlowManager.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solitardj9.timelineService.application.messageFlowManager.model.ConsumeMessage;
import com.solitardj9.timelineService.application.messageFlowManager.model.PublishMessage;
import com.solitardj9.timelineService.application.messageFlowManager.service.MessageFlowManager;
import com.solitardj9.timelineService.systemInterface.networkInterface.model.GenericRecvAck;
import com.solitardj9.timelineService.systemInterface.networkInterface.model.GenericRecvMsg;
import com.solitardj9.timelineService.systemInterface.networkInterface.model.GenericSendAck;
import com.solitardj9.timelineService.systemInterface.networkInterface.model.GenericSendMsg;
import com.solitardj9.timelineService.systemInterface.networkInterface.model.NetworkInterfaceParamEunm.GenericRecvAckParam;
import com.solitardj9.timelineService.systemInterface.networkInterface.model.NetworkInterfaceParamEunm.GenericRecvMsgParam;
import com.solitardj9.timelineService.systemInterface.networkInterface.model.NetworkInterfaceParamEunm.GenericSendAckParam;
import com.solitardj9.timelineService.systemInterface.networkInterface.model.NetworkInterfaceParamEunm.GenericSendMsgParam;

@Service("messageFlowManager")
public class MessageFlowManagerImpl implements MessageFlowManager {
	//
	private static final Logger logger = LoggerFactory.getLogger(MessageFlowManagerImpl.class);
	
	@Autowired
	ApplicationEventPublisher applicationEventPublisher;
	
	private ObjectMapper om = new ObjectMapper();
	
	@EventListener
	@Async
	public void onGenericRecvMsgEvent(GenericRecvMsg genericRecvMsg) {
		//
		logger.info("[MessageFlowManager].onGenericRecvMsgEvent : " + genericRecvMsg.toString());

		String clientId = (String)genericRecvMsg.getDataValue(GenericRecvMsgParam.CLIENT_ID.getParam());
		String consumerTag = (String)genericRecvMsg.getDataValue(GenericRecvMsgParam.CONSUMER_TAG.getParam());
		String message = (String)genericRecvMsg.getDataValue(GenericRecvMsgParam.MESSAGE.getParam());
		
		try {
			ConsumeMessage consumeMessage = om.readValue(message, ConsumeMessage.class);
			
			applicationEventPublisher.publishEvent(consumeMessage);
		} catch (JsonProcessingException e) {
			//e.printStackTrace();
			logger.error("[MessageFlowManager].onGenericRecvMsgEvent : " + e.toString());
		} finally {
			Map<String, Object> data = new HashMap<>();
			data.put(GenericRecvAckParam.CLIENT_ID.getParam(), clientId);
			data.put(GenericRecvAckParam.CONSUMER_TAG.getParam(), consumerTag);
			
			applicationEventPublisher.publishEvent(new GenericRecvAck(data));
		}
	}
	
	@EventListener
	@Async
	public void onPublishMessage(PublishMessage publishMessage) {
		//
		String message = null;
		try {
			message = om.writeValueAsString(publishMessage);
			
			Map<String, Object> data = new HashMap<>();
			
			String ackId = UUID.randomUUID().toString();
			data.put(GenericSendMsgParam.ACK_ID.getParam(), ackId);
			data.put(GenericSendMsgParam.MESSAGE.getParam(), message);
			
			logger.info("[MessageFlowManager].onPublishMessage : ack ID = " + ackId);
			
			applicationEventPublisher.publishEvent(new GenericSendMsg(data));
		} catch (JsonProcessingException e) {
			//e.printStackTrace();
			logger.error("[MessageFlowManager].onPublishMessage : " + e.toString());
		}
	}
	
	@EventListener
	@Async
	public void onGenericSendAckEvent(GenericSendAck genericSendAck) {
		//
		try {
			logger.info("[MessageFlowManager].onGenericSendAckEvent : ack ID = " + genericSendAck.getDataValue(GenericSendAckParam.ACK_ID.getParam()));
		} catch (Exception e) {
			//e.printStackTrace();
			logger.error("[MessageFlowManager].onGenericSendAckEvent : " + e.toString());
		}
	}
}