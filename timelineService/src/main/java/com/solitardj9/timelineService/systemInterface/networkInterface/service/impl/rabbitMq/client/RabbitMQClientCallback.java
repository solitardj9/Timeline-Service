package com.solitardj9.timelineService.systemInterface.networkInterface.service.impl.rabbitMq.client;

public interface RabbitMQClientCallback {
	//
	public void onMessage(String clientId, String consumerTag, String message);
}
