package com.hrs.cloud.enums;

public enum GlobalResultCodeEnum {

	SUCCESS(200),
	FAIL(500,"操作失败"),
	EXCEPTION(501,"系统异常"),
	PARAM_NEEDED(502,"参数%s缺失"),
	FREQUENT_OPERATION(503,"操作频繁，请稍后再试");




	private int code;
	private String desc;

	GlobalResultCodeEnum(int code){
		this.code = code;
	}

	GlobalResultCodeEnum(int code, String desc){
		this.code = code;
		this.desc = desc;
	}
	
	
	public int getCode(){
		return code;
	}
	
	public String getDesc(){
		return desc;
	}
	
}
