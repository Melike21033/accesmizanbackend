package com.mizanlabs.mr.exception;

import org.springframework.http.HttpStatus;

class ApiError {

    private final HttpStatus status;
    public HttpStatus getStatus() {
		return status;
	}

	private final String message;

    ApiError(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
