package com.solitardj9.timelineService.systemInterface.inMemoryInterface.model;

import com.solitardj9.timelineService.systemInterface.inMemoryInterface.service.InMemoryEventListener;

public class InMemoryInstance {
	//
	private String name;
	
	private Integer backupCount;
	
	private Boolean readBackupData;
	
	private String lockName;
	
	private InMemoryEventListener eventListener;
	
	public InMemoryInstance() {
	}

	public InMemoryInstance(String name, Integer backupCount, Boolean readBackupData, String lockName, InMemoryEventListener eventListener) {
		this.name = name;
		this.backupCount = backupCount;
		this.readBackupData = readBackupData;
		this.lockName = lockName;
		this.eventListener = eventListener;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getBackupCount() {
		return backupCount;
	}

	public void setBackupCount(Integer backupCount) {
		this.backupCount = backupCount;
	}

	public Boolean getReadBackupData() {
		return readBackupData;
	}

	public void setReadBackupData(Boolean readBackupData) {
		this.readBackupData = readBackupData;
	}

	public String getLockName() {
		return lockName;
	}

	public void setLockName(String lockName) {
		this.lockName = lockName;
	}

	public InMemoryEventListener getEventListener() {
		return eventListener;
	}

	public void setEventListener(InMemoryEventListener eventListener) {
		this.eventListener = eventListener;
	}

	@Override
	public String toString() {
		return "InMemoryInstance [name=" + name + ", backupCount=" + backupCount + ", readBackupData=" + readBackupData
				+ ", lockName=" + lockName + ", eventListener=" + eventListener + "]";
	}
}