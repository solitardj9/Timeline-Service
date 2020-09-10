package com.solitardj9.timelineService.application.timelineManager.service.exception;

public class ExceptionTimelineInternalFailure extends Exception{
	
	private static final long serialVersionUID = -4879539235477531865L;
	
	private final int ERR_CODE;
	
	public ExceptionTimelineInternalFailure() {
		//
    	super(ExceptionTimelineStatusCode.Internal_Failure.getMessage());
    	ERR_CODE = ExceptionTimelineStatusCode.Internal_Failure.getCode();
    }
    
	public ExceptionTimelineInternalFailure(Throwable cause) {
		//
		super(ExceptionTimelineStatusCode.Internal_Failure.getMessage(), cause);
		ERR_CODE = ExceptionTimelineStatusCode.Internal_Failure.getCode();
	}
	
	public int getErrCode() {
		//
		return ERR_CODE;
    }
}