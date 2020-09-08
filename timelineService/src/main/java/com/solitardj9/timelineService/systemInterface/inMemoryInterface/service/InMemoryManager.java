package com.solitardj9.timelineService.systemInterface.inMemoryInterface.service;

import com.hazelcast.core.IMap;
import com.solitardj9.timelineService.systemInterface.inMemoryInterface.model.InMemoryInstance;
import com.solitardj9.timelineService.systemInterface.inMemoryInterface.service.impl.exception.ExceptionHazelcastDataStructureCreationFailure;
import com.solitardj9.timelineService.systemInterface.inMemoryInterface.service.impl.exception.ExceptionHazelcastDataStructureNotFoundFailure;

public interface InMemoryManager {
	//
	public IMap<Object, Object> addMap(InMemoryInstance inMemoryInstance) throws ExceptionHazelcastDataStructureCreationFailure, ExceptionHazelcastDataStructureNotFoundFailure;
	
	public IMap<Object, Object> getMap(String mapName) throws ExceptionHazelcastDataStructureNotFoundFailure;
}