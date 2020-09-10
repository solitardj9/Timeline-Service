package com.solitardj9.timelineService.application.timelineManager.service.exception;

public class ExceptionTimelineConflictFailure extends Exception{
    //
	private static final long serialVersionUID = -3134830424775406028L;
	
	private final int ERR_CODE;
	
	public ExceptionTimelineConflictFailure() {
		//
    	super(ExceptionTimelineStatusCode.Conflict.getMessage());
    	ERR_CODE = ExceptionTimelineStatusCode.Conflict.getCode();
    }
    
	public ExceptionTimelineConflictFailure(Throwable cause) {
		//
		super(ExceptionTimelineStatusCode.Conflict.getMessage(), cause);
		ERR_CODE = ExceptionTimelineStatusCode.Conflict.getCode();
	}
	
	public int getErrCode() {
		//
		return ERR_CODE;
    }
}