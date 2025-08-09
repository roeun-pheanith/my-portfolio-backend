package com.nith.flex.portfolio.exception;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class ErrorResponse {
	private HttpStatus status;
	private String message;
}
