package com.solitardj9.timelineService.systemInterface.networkInterface.model;

import java.io.Serializable;
import java.util.Map;

public class GenericRecvMsg implements Serializable {
	
	private static final long serialVersionUID = 4011309716702985022L;
	
	private Map<String, Object> data;
	
	public GenericRecvMsg() {
		
	}

	public GenericRecvMsg(Map<String, Object> data) {
		this.data = data;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}
	
	public Object getDataValue(String key) {
		return data.get(key);
	}

	@Override
	public String toString() {
		return "GenericRecvMsg [data=" + data + "]";
	}
}