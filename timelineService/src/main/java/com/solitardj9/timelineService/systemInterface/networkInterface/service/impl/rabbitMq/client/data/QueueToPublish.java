package com.solitardj9.timelineService.systemInterface.networkInterface.service.impl.rabbitMq.client.data;

import java.util.Map;

public class QueueToPublish extends ToPublish {
	//
	private String queue;
	
	private boolean exclusive;

	public QueueToPublish() {
		super();
	}

	public QueueToPublish(String queue, boolean exclusive, boolean durable, boolean autoDelete, Map<String, Object> arguments) {
		super();
		this.queue = queue;
		this.exclusive = exclusive;
		setDurable(durable);
		setAutoDelete(autoDelete);
		setArguments(arguments);
	}

	public String getQueue() {
		return queue;
	}

	public void setQueue(String queue) {
		this.queue = queue;
	}

	public boolean isExclusive() {
		return exclusive;
	}

	public void setExclusive(boolean exclusive) {
		this.exclusive = exclusive;
	}

	@Override
	public String toString() {
		return "QueueToPublish [queue=" + queue + ", exclusive=" + exclusive + ", toString()=" + super.toString() + "]";
	}
}