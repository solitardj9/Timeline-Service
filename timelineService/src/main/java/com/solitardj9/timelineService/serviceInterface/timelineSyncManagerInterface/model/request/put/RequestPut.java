package com.solitardj9.timelineService.serviceInterface.timelineSyncManagerInterface.model.request.put;

public class RequestPut extends RequestPutDefault {
	//
	private Long time;
	
	private String value;
	
	public RequestPut() {
		
	}

	public RequestPut(Long time, String value) {
		this.time = time;
		this.value = value;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "RequestPut [time=" + time + ", value=" + value + "]";
	}
}