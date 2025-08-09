package com.nith.flex.portfolio.exception;

import org.springframework.http.HttpStatus;

public class ResourceException extends ApiException {

	public ResourceException(String resourceName, Long id, String errorCode) {
		super(HttpStatus.NOT_FOUND, String.format("%s with id = %d was not found", resourceName, id), errorCode);
	}

}
