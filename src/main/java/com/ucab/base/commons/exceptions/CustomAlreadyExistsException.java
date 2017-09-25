package com.ucab.base.commons.exceptions;

import org.springframework.http.HttpStatus;

import com.ucab.base.commons.enums.CustomResponseCode;

public class CustomAlreadyExistsException extends CustomBaseException {

	

	/**
	 * 
	 */
	private static final long serialVersionUID = -4026503259763678458L;

	public CustomAlreadyExistsException(String message) {
		super(message);
		setHttpStatus(HttpStatus.BAD_REQUEST);
		setCode(CustomResponseCode.ALREADY_EXISTS);
	}

}
