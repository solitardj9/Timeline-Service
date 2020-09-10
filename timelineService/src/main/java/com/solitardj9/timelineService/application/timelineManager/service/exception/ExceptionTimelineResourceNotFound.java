package com.solitardj9.timelineService.application.timelineManager.service.exception;

public class ExceptionTimelineResourceNotFound extends Exception{
	
	private static final long serialVersionUID = 2290047580774752493L;
	
	private final int ERR_CODE;
	
	public ExceptionTimelineResourceNotFound() {
		//
    	super(ExceptionTimelineStatusCode.Resource_Not_Found.getMessage());
    	ERR_CODE = ExceptionTimelineStatusCode.Resource_Not_Found.getCode();
    }
    
	public ExceptionTimelineResourceNotFound(Throwable cause) {
		//
		super(ExceptionTimelineStatusCode.Resource_Not_Found.getMessage(), cause);
		ERR_CODE = ExceptionTimelineStatusCode.Resource_Not_Found.getCode();
	}
	
	public int getErrCode() {
		//
		return ERR_CODE;
    }
}