package com.solitardj9.timelineService.application.messageFlowManager.model;

import java.io.Serializable;

public class PublishMessage implements Serializable {

	private static final long serialVersionUID = -8250443786774290835L;
	
	private String traceId;

	private String type;
	
	private String message;
	
	public PublishMessage() {
		
	}

	public PublishMessage(String traceId, String type, String message) {
		this.traceId = traceId;
		this.type = type;
		this.message = message;
	}

	public String getTraceId() {
		return traceId;
	}

	public void setTraceId(String traceId) {
		this.traceId = traceId;
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
		return "PublishMessage [traceId=" + traceId + ", type=" + type + ", message=" + message + "]";
	}
}