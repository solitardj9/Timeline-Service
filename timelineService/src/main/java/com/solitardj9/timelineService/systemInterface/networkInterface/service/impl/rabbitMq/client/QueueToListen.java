package com.solitardj9.timelineService.systemInterface.networkInterface.service.impl.rabbitMq.client;

import java.util.Map;

public class QueueToListen {
	
	private String queue;
	
	private boolean durable;
	
	private boolean exclusive;
	
	private boolean autoDelete;
	
	private Map<String, Object> arguments;

	public QueueToListen() {
		
	}

	public QueueToListen(String queue, boolean durable, boolean exclusive, boolean autoDelete,
			Map<String, Object> arguments) {
		this.queue = queue;
		this.durable = durable;
		this.exclusive = exclusive;
		this.autoDelete = autoDelete;
		this.arguments = arguments;
	}

	public String getQueue() {
		return queue;
	}

	public void setQueue(String queue) {
		this.queue = queue;
	}

	public boolean isDurable() {
		return durable;
	}

	public void setDurable(boolean durable) {
		this.durable = durable;
	}

	public boolean isExclusive() {
		return exclusive;
	}

	public void setExclusive(boolean exclusive) {
		this.exclusive = exclusive;
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
		return "QueueToListen [queue=" + queue + ", durable=" + durable + ", exclusive=" + exclusive + ", autoDelete="
				+ autoDelete + ", arguments=" + arguments + "]";
	}
}
