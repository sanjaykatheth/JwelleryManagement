package com.jewelleryshop.management.exception;

import java.io.FileNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import com.jewelleryshop.management.model.response.ExceptionHandlingResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(VendorNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<ExceptionHandlingResponse> handleVendorNotFoundException(VendorNotFoundException ex) {
		ex.printStackTrace();
		ExceptionHandlingResponse errorResponse = new ExceptionHandlingResponse(ex.getMessage());
		return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(MissingServletRequestPartException.class)
	public ResponseEntity<ExceptionHandlingResponse> handleMissingServletRequestPartException(
			MissingServletRequestPartException ex) {
		logger.error("Missing part in request", ex);
		ExceptionHandlingResponse errorResponse = new ExceptionHandlingResponse(ex.getMessage());
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(FileNotFoundException.class)
	public ResponseEntity<ExceptionHandlingResponse> handleRuntimeException(RuntimeException ex) {
		logger.error("An error occurred: ", ex);
		ExceptionHandlingResponse errorResponse = new ExceptionHandlingResponse(ex.getMessage());
		return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
	}
}