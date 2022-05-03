package com.ecommerce.training.exception;

import java.util.List;

import com.ecommerce.training.config.Errors;

public class BadRequestException extends RuntimeException {

	private List<Errors> errors;

	public List<Errors> getErrors() {
		return errors;
	}

	public void setErrors(List<Errors> errors) {
		this.errors = errors;
	}

	public BadRequestException(String message,List<Errors> errors) {
		super(message);
		this.errors = errors;
	}
	
}
