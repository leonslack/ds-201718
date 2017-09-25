package com.ucab.base.commons.enums;

public enum CustomResponseCode {

	OK(0, "OK"), 
	MISSING_ATTRIBUTE(10, "Missing Attribute"), 
	REGISTRY_NOTFOUND(20, "Record Not found") , 
	INVALID_ATTRIBUTE(30, "Invalid Attribute"), 
	INVALID_ACTION(31, "Invalid Action"),
	ALREADY_EXISTS(40, "Registry with given attributes already exists"), 
	DATABASE_ERROR(100, "Database Operation Error"),
	STORAGE_ERROR(150, "Error connecting to Storage server"),
	CONNECTION_ERROR(155, "Error connecting the server or service"),
    MESSAGING_ERROR(160, "Error sending Email"),
    MALFORMED_TEMPLATE(300, "Malformed Template");
	
	
	public int getCode() {
		return code;
	}
	public String getMessage(){
		return message;
	}
	
	private int code;

	private String message;
	
	private CustomResponseCode(int code, String message) {
		this.code = code;
		this.message = message;
	}
}
