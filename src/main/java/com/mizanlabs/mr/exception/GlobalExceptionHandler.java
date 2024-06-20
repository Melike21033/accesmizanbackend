package com.mizanlabs.mr.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // Example of handling a custom exception
    @ExceptionHandler(MyCustomException.class)
    public ResponseEntity<Object> handleMyCustomException(MyCustomException ex) {
        // Create a response structure
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage());
        return buildResponseEntity(apiError);
    }

    // Example of handling IllegalStateException
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Object> handleIllegalStateException(IllegalStateException ex) {
        // Create a response structure
        ApiError apiError = new ApiError(HttpStatus.CONFLICT, ex.getMessage());
        return buildResponseEntity(apiError);
    }

    // Example of handling all other exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAll(Exception ex) {
        // Create a response structure
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred");
        return buildResponseEntity(apiError);
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}

