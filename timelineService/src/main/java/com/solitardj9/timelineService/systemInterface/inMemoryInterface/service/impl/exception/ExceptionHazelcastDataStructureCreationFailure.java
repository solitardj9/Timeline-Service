package com.solitardj9.timelineService.systemInterface.inMemoryInterface.service.impl.exception;

public class ExceptionHazelcastDataStructureCreationFailure extends Exception{
    //
	private static final long serialVersionUID = 7594371506466712673L;
	
	private final int ERR_CODE;
	
	public ExceptionHazelcastDataStructureCreationFailure() {
		//
    	super(ExceptionHazelcastStatusCode.BAD_REQUEST.getMessage());
    	ERR_CODE = ExceptionHazelcastStatusCode.BAD_REQUEST.getCode();
    }
    
	public ExceptionHazelcastDataStructureCreationFailure(Throwable cause) {
		//
		super(ExceptionHazelcastStatusCode.BAD_REQUEST.getMessage(), cause);
		ERR_CODE = ExceptionHazelcastStatusCode.BAD_REQUEST.getCode();
	}
	
	public int getErrCode() {
		//
		return ERR_CODE;
    }
}