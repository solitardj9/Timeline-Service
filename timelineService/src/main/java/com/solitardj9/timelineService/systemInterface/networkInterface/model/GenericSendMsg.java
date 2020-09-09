package com.solitardj9.timelineService.systemInterface.networkInterface.model;

import java.io.Serializable;
import java.util.Map;

public class GenericSendMsg implements Serializable {
	
	private static final long serialVersionUID = -7337723613995627870L;
	
	private Map<String, Object> data;
	
	public GenericSendMsg() {
		
	}

	public GenericSendMsg(Map<String, Object> data) {
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
		return "GenericSendMsg [data=" + data + "]";
	}
}