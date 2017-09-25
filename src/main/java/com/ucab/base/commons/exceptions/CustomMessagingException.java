package com.ucab.base.commons.exceptions;

import org.springframework.http.HttpStatus;

import com.ucab.base.commons.enums.CustomResponseCode;

public class CustomMessagingException extends CustomBaseException {

	
	private static final long serialVersionUID = 8212089814413897606L;

	public CustomMessagingException(String message) {
		super(message);
		setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		setCode(CustomResponseCode.MESSAGING_ERROR);
	}

}
