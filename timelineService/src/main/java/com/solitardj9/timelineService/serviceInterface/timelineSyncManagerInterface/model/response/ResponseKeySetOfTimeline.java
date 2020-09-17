package com.solitardj9.timelineService.serviceInterface.timelineSyncManagerInterface.model.response;

import java.util.Set;

public class ResponseKeySetOfTimeline extends ResponseDefualt {
	//
	private Set<Long> keySet;
	
	public ResponseKeySetOfTimeline() {
		
	}

	public ResponseKeySetOfTimeline(Set<Long> keySet) {
		this.keySet = keySet;
	}
	
	public ResponseKeySetOfTimeline(Integer status, Set<Long> keySet) {
		this.keySet = keySet;
		setStatus(status);
	}

	public Set<Long> getKeySet() {
		return keySet;
	}

	public void setKeySet(Set<Long> keySet) {
		this.keySet = keySet;
	}

	@Override
	public String toString() {
		return "ResponseKeySetOfTimelines [keySet=" + keySet + ", toString()=" + super.toString() + "]";
	}
}