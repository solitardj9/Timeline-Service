package com.solitardj9.timelineService.application.messageFlowManager.model;

import java.io.Serializable;

public class ConsumeMessage implements Serializable {

	private static final long serialVersionUID = -1446660952368045468L;

	private String type;
	
	private String message;
	
	public ConsumeMessage() {
		
	}
	
	public ConsumeMessage(String type, String message) {
		this.type = type;
		this.message = message;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "PublishMessage [type=" + type + ", message=" + message + "]";
	}
}