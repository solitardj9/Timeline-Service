package com.solitardj9.timelineService.application.messageFlowManager.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ConsumeMessage implements Serializable {

	private static final long serialVersionUID = -1446660952368045468L;
	
	@JsonIgnore
	private String clientId;
	
	@JsonIgnore
	private String consumerTag;

	private String type;
	
	private String message;
	
	public ConsumeMessage() {
		
	}
	
	public ConsumeMessage(String type, String message) {
		this.type = type;
		this.message = message;
	}
	
	public ConsumeMessage(String clientId, String consumerTag, String type, String message) {
		this.clientId = clientId;
		this.consumerTag = consumerTag;
		this.type = type;
		this.message = message;
	}
	
	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getConsumerTag() {
		return consumerTag;
	}

	public void setConsumerTag(String consumerTag) {
		this.consumerTag = consumerTag;
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
		return "ConsumeMessage [clientId=" + clientId + ", consumerTag=" + consumerTag + ", type=" + type + ", message="
				+ message + "]";
	}
}