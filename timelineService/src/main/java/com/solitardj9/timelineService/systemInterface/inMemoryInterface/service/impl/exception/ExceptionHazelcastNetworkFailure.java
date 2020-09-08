package com.solitardj9.timelineService.systemInterface.inMemoryInterface.service.impl.exception;

public class ExceptionHazelcastNetworkFailure extends Exception{
    //
	private static final long serialVersionUID = 7594371506466712673L;
	
	private final int ERR_CODE;
	
	public ExceptionHazelcastNetworkFailure() {
		//
    	super(ExceptionHazelcastStatusCode.Internal_Failure.getMessage());
    	ERR_CODE = ExceptionHazelcastStatusCode.Internal_Failure.getCode();
    }
    
	public ExceptionHazelcastNetworkFailure(Throwable cause) {
		//
		super(ExceptionHazelcastStatusCode.Internal_Failure.getMessage(), cause);
		ERR_CODE = ExceptionHazelcastStatusCode.Internal_Failure.getCode();
	}
	
	public int getErrCode() {
		//
		return ERR_CODE;
    }
}