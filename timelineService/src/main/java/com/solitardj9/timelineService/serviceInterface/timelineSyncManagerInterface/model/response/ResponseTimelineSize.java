package com.solitardj9.timelineService.serviceInterface.timelineSyncManagerInterface.model.response;

public class ResponseTimelineSize extends ResponseDefualt {
	//
	private Integer size;
	
	public ResponseTimelineSize() {
		
	}

	public ResponseTimelineSize(Integer size) {
		this.size = size;
	}
	
	public ResponseTimelineSize(Integer status, Integer size) {
		this.size = size;
		setStatus(status);
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	@Override
	public String toString() {
		return "ResponseTimeline [size=" + size + ", toString()=" + super.toString() + "]";
	}
}