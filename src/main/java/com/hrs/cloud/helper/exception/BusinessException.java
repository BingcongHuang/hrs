package com.hrs.cloud.helper.exception;

/**
 * 	业务异常
 * 
 *  异常信息可供前端弹窗提示给用户
 *
 */

public class BusinessException extends RuntimeException{

	private String alertMsg;

	private Integer statusCode;
	
	public BusinessException(String alertMsg){
		super(alertMsg);
		this.alertMsg = alertMsg;
	}

	public BusinessException(String alertMsg, Integer statusCode){
		super(alertMsg);
		this.alertMsg = alertMsg;
		this.statusCode = statusCode;
	}
	
	public BusinessException(String alertMsg, String errMsg){
		super(errMsg);
		this.alertMsg = alertMsg;
	}
	
	public BusinessException(String alertMsg, String errMsg, Throwable e){
		super(errMsg,e);
		this.alertMsg = alertMsg;
	}
	
	public BusinessException(String alertMsg, Throwable e){
		super(alertMsg,e);
		this.alertMsg = alertMsg;
	}
	
	public BusinessException(Throwable e){
		super(e);
	}
	
	public String getAlertMsg(){
		return alertMsg;
	}

	public Integer getStatusCode(){
		return statusCode;
	}


}
