package com.ucab.base.webservices.dtos.response.base;


import org.apache.http.HttpStatus;

import com.ucab.base.commons.enums.CustomResponseCode;
import com.ucab.base.commons.exceptions.CustomBaseException;

public class AbstractBaseResponseStructure {

	private int statusCode;

	private String message;	

	public AbstractBaseResponseStructure() {
		super();
		this.setStatusCode((int) CustomResponseCode.OK.getCode());
		this.message= CustomResponseCode.OK.getMessage();
	}	
	public AbstractBaseResponseStructure(CustomBaseException cbe){
		super();
		this.statusCode = cbe.getCode().getCode();
	}

	public AbstractBaseResponseStructure(Exception e){
		super();
		this.statusCode = HttpStatus.SC_INTERNAL_SERVER_ERROR;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}

