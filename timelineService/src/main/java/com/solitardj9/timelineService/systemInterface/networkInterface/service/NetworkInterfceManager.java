package com.solitardj9.timelineService.systemInterface.networkInterface.service;

import java.util.Map;

public interface NetworkInterfceManager {
	//
	public void disconnectAdminClients();
	
	public void createExchange(String exchange, String type, boolean durable, boolean autoDelete, Map<String, Object> arguments);
	
	public void deleteExchange(String exchange);
	
	public void createQueue(String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments);

	public void deleteQueue(String queue);
	
	public void bindQueueWithExchange(String queue, String exchange, String routingKey);

	public void unbindQueueFromExchange(String queue, String exchange, String routingKey);
	
	
	
	
	
}