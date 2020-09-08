package com.solitardj9.timelineService.systemInterface.networkInterface.service.impl.rabbitMq.client.exception;

public class ExceptionRabbitMQClientConnectionFailure extends Exception{
    //
	private static final long serialVersionUID = 7594371506466712673L;
	
	private final int ERR_CODE;
	
	public ExceptionRabbitMQClientConnectionFailure() {
		//
    	super(ExceptionRabbitMQClientStatusCode.Resource_Not_Found.getMessage());
    	ERR_CODE = ExceptionRabbitMQClientStatusCode.Resource_Not_Found.getCode();
    }
    
	public ExceptionRabbitMQClientConnectionFailure(Throwable cause) {
		//
		super(ExceptionRabbitMQClientStatusCode.Resource_Not_Found.getMessage(), cause);
		ERR_CODE = ExceptionRabbitMQClientStatusCode.Resource_Not_Found.getCode();
	}
	
	public int getErrCode() {
		//
		return ERR_CODE;
    }
}