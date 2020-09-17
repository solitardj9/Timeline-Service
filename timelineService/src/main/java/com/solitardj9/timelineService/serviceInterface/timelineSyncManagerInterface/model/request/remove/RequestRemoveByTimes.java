package com.solitardj9.timelineService.serviceInterface.timelineSyncManagerInterface.model.request.remove;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("removeByTimes")
public class RequestRemoveByTimes extends RequestRemoveDefault {
	//
	private List<Long> times;
	
	public RequestRemoveByTimes() {
		
	}

	public RequestRemoveByTimes(List<Long> times) {
		this.times = times;
	}
	
	public RequestRemoveByTimes(String type, List<Long> times) {
		setType(type);
		this.times = times;
	}

	public List<Long> getTimes() {
		return times;
	}

	public void setTimes(List<Long> times) {
		this.times = times;
	}

	@Override
	public String toString() {
		return "RequestRemoveByTimes [times=" + times + ", toString()=" + super.toString()
				+ "]";
	}
}