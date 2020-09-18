package com.solitardj9.timelineService.systemInterface.httpInterface.model;

public class HttpProxyAdaptorParamEnum {
	//
	public enum RestTemplateRequestFactoryMode {
	    //
		NORMAL("normal"),
		FILE("file")
		;
		
		private String mode;
		
		private RestTemplateRequestFactoryMode(String mode) {
			this.mode = mode;
	    }
		
		public String getMode() {
			return mode;
	    }
		
		@Override
		public String toString() {
			return mode;
	    }
	}
}
