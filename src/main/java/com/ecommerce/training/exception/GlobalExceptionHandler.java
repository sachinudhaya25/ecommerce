package com.ecommerce.training.exception;



import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.ecommerce.training.config.GenericResponse;
import com.ecommerce.training.payload.response.MessageResponse;


@ControllerAdvice
public class GlobalExceptionHandler  extends ResponseEntityExceptionHandler  {

	  private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	  @ExceptionHandler
	  public ResponseEntity handleException(Exception ex) {
		  logger.error("Default Exception");
		  Map<String, Object> response = new HashMap<>();
		  response.put("Title", HttpStatus.INTERNAL_SERVER_ERROR.name());
		  response.put("Message", "oops..something went wrong! ");   
	      response.put("Error code", HttpStatus.INTERNAL_SERVER_ERROR);
	      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	    }
	  
	  @ExceptionHandler(value = { InvalidInputException.class })
	  public ResponseEntity<Object> handleInvalidInputException(InvalidInputException ex) {
		  logger.error("Invalid Input Exception");
		  Map<String, Object> response = new HashMap<>();
	      response.put("Error code", HttpStatus.BAD_REQUEST.value());
	      response.put("Message", ex.getErrorMessage());   
	      
	      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	  
	    }
	  @ExceptionHandler(value = { BadRequestException.class })
	  public ResponseEntity handleResourceNotFoundException(BadRequestException ex) {
		  logger.error("Data Not Null Exception",ex.getMessage());
		  Map<String, Object> response = new HashMap<>();
	      response.put("Error code", HttpStatus.BAD_REQUEST.value());
	      response.put("Message", ex.getMessage());   
	      response.put("Errors", ex.getErrors());  
	      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	    }
	  @ExceptionHandler(value = { Unauthorized.class })
	  public ResponseEntity<Object> handleUnauthorizedException(Unauthorized ex) {
		  logger.error("Unauthorized Exception: ");
	          return new ResponseEntity<Object>("Unauthorized Exception",HttpStatus.BAD_REQUEST);
	    }
//	  @ExceptionHandler(value = { ResourceNotFoundException.class })
//	  public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex) {
//		  logger.error("Resource Not Found Exception",ex.getMessage());
//	      return new ResponseEntity<Object>(new MessageResponse(ex.getMessage()),HttpStatus.NOT_FOUND);
//	    }
	  @ExceptionHandler(value = { ResourceNotFoundException.class })
	  public ResponseEntity handleResourceNotFoundException(ResourceNotFoundException ex) {
		  logger.error("Resource Not Found Exception",ex.getMessage());
		  GenericResponse response=new GenericResponse();
		  response.setMessage(ex.getMessage());
		  response.setStatus(HttpStatus.NOT_FOUND.name());
	      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	    }
	  @Override
		protected ResponseEntity<Object> handleMethodArgumentNotValid(
				MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
			 Map<String, String> errors = new HashMap<>();
			
			 ex.getBindingResult().getAllErrors().forEach((error) -> {
			        String fieldName = ((FieldError) error).getField();
			        String errorMessage = error.getDefaultMessage();
			        errors.put(fieldName, errorMessage);
			 });
			return new ResponseEntity<Object>(errors,HttpStatus.BAD_REQUEST);
		}
	  
	 
}
