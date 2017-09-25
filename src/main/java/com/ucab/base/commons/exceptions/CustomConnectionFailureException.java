package com.ucab.base.commons.exceptions;

import org.springframework.http.HttpStatus;

import com.ucab.base.commons.enums.CustomResponseCode;

public class CustomConnectionFailureException extends CustomBaseException {

	private static final long serialVersionUID = 3070196043579916774L;

	public CustomConnectionFailureException(String message) {
		super(message);
		setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		setCode(CustomResponseCode.CONNECTION_ERROR);
	}
	
}
