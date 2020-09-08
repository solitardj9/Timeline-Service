package com.solitardj9.timelineService.systemInterface.networkInterface.service.impl.rabbitMq.client;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;

public class RabbitMQPublisher {
	
//	@Getter
//	private String clientId;
//	
//    private Channel channel;
//
//    public RabbitMQPublisher(String clientId, Channel channel) throws IOException {
//        //
//    	this.clientId = clientId;
//        this.channel = channel;
//    }
//    
//    public void doSend(String exchangeName, String topic, String message) throws IOException, TimeoutException {
//        //
//    	if (exchangeName == null || exchangeName.isEmpty()) sendMsg(topic, message);
//    	else {
//    		channel.exchangeDeclare(exchangeName, "topic", true, false, null);
//            channel.basicPublish(exchangeName, topic, null, message.getBytes("UTF-8"));
//    	}
//    }
//    
//    private void sendMsg(String queueName, String message) throws IOException {
////    	channel.queueDeclare(queueName, true, false, false, null);
//    	channel.basicPublish("", queueName, null, message.getBytes("UTF-8"));
//    }
//    
//    public Long getNextPublishSeqNo() {
//    	return channel.getNextPublishSeqNo();
//    }
    
    
}