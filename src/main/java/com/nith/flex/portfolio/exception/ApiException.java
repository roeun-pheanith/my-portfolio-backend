package com.nith.flex.portfolio.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {
	private final HttpStatus status;
	private final LocalDateTime timestemp = LocalDateTime.now();
	private final String errorCode;

	public ApiException(HttpStatus status, String message, String errorCode) {
		super(message);
		this.status = status;
		this.errorCode = errorCode;
	}

}
