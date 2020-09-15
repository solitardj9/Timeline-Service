package com.solitardj9.timelineService.application.messageFlowManager.model;

import java.io.Serializable;

public class PublishAck implements Serializable {

	private static final long serialVersionUID = 1189873982759418601L;
	
	private String traceId;
	
	public PublishAck() {
		
	}

	public PublishAck(String traceId) {
		this.traceId = traceId;
	}

	public String getTraceId() {
		return traceId;
	}

	public void setTraceId(String traceId) {
		this.traceId = traceId;
	}

	@Override
	public String toString() {
		return "PublishAck [traceId=" + traceId + "]";
	}
}