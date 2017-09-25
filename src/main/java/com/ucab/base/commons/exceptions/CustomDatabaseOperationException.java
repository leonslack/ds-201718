package com.ucab.base.commons.exceptions;

import org.springframework.http.HttpStatus;

import com.ucab.base.commons.enums.CustomResponseCode;

public class CustomDatabaseOperationException extends CustomBaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1298167912806448074L;

	public CustomDatabaseOperationException(String message) {
		super(message);
		setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		setCode(CustomResponseCode.DATABASE_ERROR);
	}

}
