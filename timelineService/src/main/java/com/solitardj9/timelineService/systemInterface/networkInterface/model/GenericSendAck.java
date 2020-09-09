package com.solitardj9.timelineService.systemInterface.networkInterface.model;

import java.io.Serializable;
import java.util.Map;

public class GenericSendAck implements Serializable {

	private static final long serialVersionUID = 7454293924185338120L;

	private Map<String, Object> data;
	
	public GenericSendAck() {
		
	}

	public GenericSendAck(Map<String, Object> data) {
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
		return "GenericSendAck [data=" + data + "]";
	}
}
