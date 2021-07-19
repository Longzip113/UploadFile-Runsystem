package com.example.demo.exception;

import java.util.Date;

public class ErrorResponse {
	private String message;
	private int statusCode;
	private Date timestamp;
	private String description;
	
	public ErrorResponse(String message, int statusCode, Date timestamp, String description) {
		super();
		this.message = message;
		this.statusCode = statusCode;
		this.timestamp = timestamp;
		this.description = description;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public String getDescription() {
		return description;
	}

	public String getMessage() {
		return message;
	}
}
