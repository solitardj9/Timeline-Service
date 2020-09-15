package com.solitardj9.timelineService.serviceInterface.timelineSyncManagerInterface.model;

public class ResponseDefualt {
	//
	private Integer status;
	
	public ResponseDefualt() {
		
	}
	
	public ResponseDefualt(Integer status) {
		this.status = status;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "ResponseDefualt [status=" + status + "]";
	}
}