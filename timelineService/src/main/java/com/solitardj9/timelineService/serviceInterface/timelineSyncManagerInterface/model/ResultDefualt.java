package com.solitardj9.timelineService.serviceInterface.timelineSyncManagerInterface.model;

public class ResultDefualt {
	//
	private Integer status;
	
	public ResultDefualt() {
		
	}
	
	public ResultDefualt(Integer status) {
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
		return "ResultDefualt [status=" + status + "]";
	}
}