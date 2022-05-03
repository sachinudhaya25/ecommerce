package com.ecommerce.training.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class Unauthorized extends RuntimeException {

	private static final long serialVersionUID = 1L;
	public String errorMessage;
//	public Unauthorized(String errorMessage) {
//		super();
//		this.errorMessage = errorMessage;
//	}
}
