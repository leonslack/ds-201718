package com.ucab.base.commons.exceptions;

import org.springframework.http.HttpStatus;

import com.ucab.base.commons.enums.CustomResponseCode;

public class CustomStorageOperationException extends CustomBaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1298167912806448074L;

	public CustomStorageOperationException(String message) {
		super(message);
		setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		setCode(CustomResponseCode.STORAGE_ERROR);
	}

}
