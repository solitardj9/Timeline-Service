package com.solitardj9.timelineService.application.messageFlowManager.model;

import java.io.Serializable;

public class ConsumeAck implements Serializable {

	private static final long serialVersionUID = 6050600885233361836L;

	private String clientId;
	
	private String consumerTag;

	public ConsumeAck() {
		
	}
	
	public ConsumeAck(String clientId, String consumerTag) {
		this.clientId = clientId;
		this.consumerTag = consumerTag;
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

	@Override
	public String toString() {
		return "ConsumeAck [clientId=" + clientId + ", consumerTag=" + consumerTag + "]";
	}
}