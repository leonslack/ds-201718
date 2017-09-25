package com.ucab.base.commons.exceptions;

import org.springframework.http.HttpStatus;

import com.ucab.base.commons.enums.CustomResponseCode;

public class CustomRegistryNotFoundException extends CustomBaseException {

	private static final long serialVersionUID = -8859742897445159866L;

	public CustomRegistryNotFoundException(String message) {
		super(message);
		setHttpStatus(HttpStatus.BAD_REQUEST);
		setCode(CustomResponseCode.REGISTRY_NOTFOUND);
	}

}
