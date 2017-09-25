package com.ucab.base.commons.exceptions;

import org.springframework.http.HttpStatus;

import com.ucab.base.commons.enums.CustomResponseCode;

public abstract class CustomBaseException extends Exception {

	/**
	 *
	 */
	private static final long serialVersionUID = 8562765588375119672L;

	private CustomResponseCode code;
	private HttpStatus httpStatus;

	public CustomBaseException(String message) {
		super(message);
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}
	
	public CustomResponseCode getCode() {
		return code;
	}
	
	public void setCode(CustomResponseCode code) {
		this.code = code;
	}
}
