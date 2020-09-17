package com.solitardj9.timelineService.serviceInterface.timelineSyncManagerInterface.model.response;

public class ResponseTimelineEmpty extends ResponseDefualt {
	//
	private Boolean isEmpty;
	
	public ResponseTimelineEmpty() {
		
	}

	public ResponseTimelineEmpty(Boolean isEmpty) {
		this.isEmpty = isEmpty;
	}
	
	public ResponseTimelineEmpty(Integer status, Boolean isEmpty) {
		this.isEmpty = isEmpty;
		setStatus(status);
	}

	public Boolean getIsEmpty() {
		return isEmpty;
	}

	public void setIsEmpty(Boolean isEmpty) {
		this.isEmpty = isEmpty;
	}

	@Override
	public String toString() {
		return "ResponseTimelineEmpty [isEmpty=" + isEmpty + ", toString()=" + super.toString() + "]";
	}
}