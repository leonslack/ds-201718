package com.ucab.base.commons.exceptions;

import org.springframework.http.HttpStatus;

import com.ucab.base.commons.enums.CustomResponseCode;

public class CustomMissingAttributeException extends CustomBaseException {

	private static final long serialVersionUID = 6965229698022307627L;

	public CustomMissingAttributeException(String message) {
		super(message);
		setHttpStatus(HttpStatus.BAD_REQUEST);
		setCode(CustomResponseCode.MISSING_ATTRIBUTE);
	}

}
