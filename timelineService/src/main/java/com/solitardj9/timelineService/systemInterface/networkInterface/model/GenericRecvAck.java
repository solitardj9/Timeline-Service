package com.solitardj9.timelineService.systemInterface.networkInterface.model;

import java.io.Serializable;
import java.util.Map;

public class GenericRecvAck implements Serializable {

	private static final long serialVersionUID = -5447572552857302127L;

	private Map<String, Object> data;
	
	public GenericRecvAck() {
		
	}

	public GenericRecvAck(Map<String, Object> data) {
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
		return "GenericRecvAck [data=" + data + "]";
	}
}
