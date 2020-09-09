package com.solitardj9.timelineService.systemInterface.networkInterface.service.impl.rabbitMq.client.data;

import java.util.Map;

public class ToPublish {
	//
	private boolean durable;
	
	private boolean autoDelete;
	
	private Map<String, Object> arguments;

	public ToPublish() {
		
	}

	public ToPublish(boolean durable, boolean autoDelete, Map<String, Object> arguments) {
		this.durable = durable;
		this.autoDelete = autoDelete;
		this.arguments = arguments;
	}

	public boolean isDurable() {
		return durable;
	}

	public void setDurable(boolean durable) {
		this.durable = durable;
	}

	public boolean isAutoDelete() {
		return autoDelete;
	}

	public void setAutoDelete(boolean autoDelete) {
		this.autoDelete = autoDelete;
	}

	public Map<String, Object> getArguments() {
		return arguments;
	}

	public void setArguments(Map<String, Object> arguments) {
		this.arguments = arguments;
	}

	@Override
	public String toString() {
		return "ToPublish [durable=" + durable + ", autoDelete=" + autoDelete + ", arguments=" + arguments + "]";
	}
}