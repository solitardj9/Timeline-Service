package com.solitardj9.timelineService.systemInterface.inMemoryInterface.service;

import com.hazelcast.map.listener.EntryAddedListener;
import com.hazelcast.map.listener.EntryRemovedListener;
import com.hazelcast.map.listener.EntryUpdatedListener;

public interface InMemoryEventListener extends EntryAddedListener<Object, Object>, EntryRemovedListener<Object, Object>, EntryUpdatedListener<Object, Object> {
	//
}