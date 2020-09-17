package com.solitardj9.timelineService.serviceInterface.timelineSyncManagerInterface.model.response;

import java.util.Set;

public class ResponseKeySetOfTimelines extends ResponseDefualt {
	//
	private Set<String> keySet;
	
	public ResponseKeySetOfTimelines() {
		
	}

	public ResponseKeySetOfTimelines(Set<String> keySet) {
		this.keySet = keySet;
	}
	
	public ResponseKeySetOfTimelines(Integer status, Set<String> keySet) {
		this.keySet = keySet;
		setStatus(status);
	}

	public Set<String> getKeySet() {
		return keySet;
	}

	public void setKeySet(Set<String> keySet) {
		this.keySet = keySet;
	}

	@Override
	public String toString() {
		return "ResponseKeySetOfTimelines [keySet=" + keySet + ", toString()=" + super.toString() + "]";
	}
}