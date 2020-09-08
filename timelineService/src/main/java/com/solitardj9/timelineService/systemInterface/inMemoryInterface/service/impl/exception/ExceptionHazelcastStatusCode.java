package com.solitardj9.timelineService.systemInterface.inMemoryInterface.service.impl.exception;

import java.util.HashMap;
import java.util.Map;


public enum ExceptionHazelcastStatusCode {
    //
	BAD_REQUEST(400, "BadRequest."),
	Resource_Not_Found(404, "ResourceNotFoundException."),
	Internal_Failure(500, "InternalFailureException.")
    ;
 
    private Integer code;
    private String message;
 
    ExceptionHazelcastStatusCode(Integer code, String msg) {
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