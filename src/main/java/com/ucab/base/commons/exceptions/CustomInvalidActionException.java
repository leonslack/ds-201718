package com.ucab.base.commons.exceptions;

import org.springframework.http.HttpStatus;

import com.ucab.base.commons.enums.CustomResponseCode;

public class CustomInvalidActionException extends CustomBaseException {


	private static final long serialVersionUID = 5469037201305095475L;

	public CustomInvalidActionException(String message) {
		super(message);
		setHttpStatus(HttpStatus.BAD_REQUEST);
		setCode(CustomResponseCode.INVALID_ACTION);
	}

}