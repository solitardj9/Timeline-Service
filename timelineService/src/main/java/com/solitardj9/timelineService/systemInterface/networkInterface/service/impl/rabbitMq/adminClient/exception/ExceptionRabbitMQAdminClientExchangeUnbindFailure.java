package com.solitardj9.timelineService.systemInterface.networkInterface.service.impl.rabbitMq.adminClient.exception;

public class ExceptionRabbitMQAdminClientExchangeUnbindFailure extends Exception{
    //
	private static final long serialVersionUID = 7594371506466712673L;
	
	private final int ERR_CODE;
	
	public ExceptionRabbitMQAdminClientExchangeUnbindFailure() {
		//
    	super(ExceptionRabbitMQAdminClientStatusCode.Internal_Failure.getMessage());
    	ERR_CODE = ExceptionRabbitMQAdminClientStatusCode.Internal_Failure.getCode();
    }
    
	public ExceptionRabbitMQAdminClientExchangeUnbindFailure(Throwable cause) {
		//
		super(ExceptionRabbitMQAdminClientStatusCode.Internal_Failure.getMessage(), cause);
		ERR_CODE = ExceptionRabbitMQAdminClientStatusCode.Internal_Failure.getCode();
	}
	
	public int getErrCode() {
		//
		return ERR_CODE;
    }
}