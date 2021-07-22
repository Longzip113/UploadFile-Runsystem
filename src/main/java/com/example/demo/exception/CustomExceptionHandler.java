package com.example.demo.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

public class CustomExceptionHandler {

	// Handle all NotFoundException 404
	@ExceptionHandler(FileStorageException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErrorResponse handlerNotFoundException(FileStorageException ex, WebRequest req) {
		ErrorResponse message = new ErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value(), new Date(),
				req.getDescription(false));
		return message;
	}
}
