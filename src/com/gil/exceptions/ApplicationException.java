package com.gil.exceptions;

import com.gil.enums.ErrorType;

public class ApplicationException extends Exception {

	
	private static final long serialVersionUID = 1L;
	
	private ErrorType errorType;

	public ApplicationException(ErrorType errorType) {
		super();
		this.errorType = errorType;
	}

	public ApplicationException(ErrorType errorType, String message) {
		super(message);
		this.errorType = errorType;
	}

	public ApplicationException(ErrorType errorType, Exception e, String message) {
		super(message, e);
		this.errorType = errorType;
	}

	public ErrorType getErrorType() {
		return errorType;
	}

}
