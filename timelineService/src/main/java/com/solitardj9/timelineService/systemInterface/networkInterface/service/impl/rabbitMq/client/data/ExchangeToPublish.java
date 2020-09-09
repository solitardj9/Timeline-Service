package com.solitardj9.timelineService.systemInterface.networkInterface.service.impl.rabbitMq.client.data;

import java.util.Map;

public class ExchangeToPublish extends ToPublish {
	//
	private String exchange;
	
	private String type;
	
	private String routingKey;

	public ExchangeToPublish() {
		super();
	}

	public ExchangeToPublish(String exchange, String type, String routingKey, boolean durable, boolean autoDelete, Map<String, Object> arguments) {
		super();
		this.exchange = exchange;
		this.type = type;
		this.routingKey = routingKey;
		setDurable(durable);
		setAutoDelete(autoDelete);
		setArguments(arguments);
	}

	public String getExchange() {
		return exchange;
	}

	public void setExchange(String exchange) {
		this.exchange = exchange;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRoutingKey() {
		return routingKey;
	}

	public void setRoutingKey(String routingKey) {
		this.routingKey = routingKey;
	}

	@Override
	public String toString() {
		return "ExchangeToPublish [exchange=" + exchange + ", type=" + type + ", routingKey=" + routingKey
				+ ", toString()=" + super.toString() + "]";
	}
}