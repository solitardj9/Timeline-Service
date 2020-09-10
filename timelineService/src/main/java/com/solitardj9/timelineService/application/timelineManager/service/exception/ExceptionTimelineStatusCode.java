package com.solitardj9.timelineService.application.timelineManager.service.exception;

import java.util.HashMap;
import java.util.Map;


public enum ExceptionTimelineStatusCode {
    //
	Resource_Not_Found(404, "ResourceNotFoundException."),
	Conflict (409, "conflict"),
	Internal_Failure(500, "InternalFailureException.")
    ;
 
    private Integer code;
    private String message;
 
    ExceptionTimelineStatusCode(Integer code, String msg) {
        this.code = code;
        this.message = msg;
    }
    public Integer getCode() {
        return this.code;
    }
    public String getMessage() {
        return this.message;
    }
    
    public Map<String,String> getMapMessage() {
		Map<String, String> messageMap = new HashMap<>();
		messageMap.put("message", this.getMessage());
		return messageMap;
	}
}