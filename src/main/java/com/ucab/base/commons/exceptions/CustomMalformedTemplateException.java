package com.ucab.base.commons.exceptions;

import org.springframework.http.HttpStatus;

import com.ucab.base.commons.enums.CustomResponseCode;

public class CustomMalformedTemplateException extends CustomBaseException {

	
	public CustomMalformedTemplateException(String message) {
		super(message);
		setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		setCode(CustomResponseCode.MALFORMED_TEMPLATE);
	}

	private static final long serialVersionUID = 6095955726540409112L;
	
}
