package com.solitardj9.timelineService.systemInterface.networkInterface.model;

public class NetworkInterfaceParamEunm {
	//
	public enum GenericRecvMsgParam {
	    //
		CLIENT_ID("clientId"),
		CONSUMER_TAG("consumerTag"),
		MESSAGE("message"),
		;
		
		private String param;
		
		private GenericRecvMsgParam(String param) {
			this.param = param;
	    }
		
		public String getParam() {
			return param;
	    }
		
		@Override
		public String toString() {
			return param;
	    }
	}
	
	public enum GenericRecvAckParam {
	    //
		CLIENT_ID("clientId"),
		CONSUMER_TAG("consumerTag")
		;
		
		private String param;
		
		private GenericRecvAckParam(String param) {
			this.param = param;
	    }
		
		public String getParam() {
			return param;
	    }
		
		@Override
		public String toString() {
			return param;
	    }
	}
}