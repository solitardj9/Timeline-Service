package com.solitardj9.timelineService.systemInterface.networkInterface.service.impl.rabbitMq.adminClient.exception;

public class ExceptionRabbitMQAdminClientDisconnectionFailure extends Exception{
    //
	private static final long serialVersionUID = 7594371506466712673L;
	
	private final int ERR_CODE;
	
	public ExceptionRabbitMQAdminClientDisconnectionFailure() {
		//
    	super(ExceptionRabbitMQAdminClientStatusCode.Resource_Not_Found.getMessage());
    	ERR_CODE = ExceptionRabbitMQAdminClientStatusCode.Resource_Not_Found.getCode();
    }
    
	public ExceptionRabbitMQAdminClientDisconnectionFailure(Throwable cause) {
		//
		super(ExceptionRabbitMQAdminClientStatusCode.Resource_Not_Found.getMessage(), cause);
		ERR_CODE = ExceptionRabbitMQAdminClientStatusCode.Resource_Not_Found.getCode();
	}
	
	public int getErrCode() {
		//
		return ERR_CODE;
    }
}