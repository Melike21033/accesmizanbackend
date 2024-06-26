package com.mizanlabs.mr.exception;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MyCustomException.class)
    public ResponseEntity<Object> handleMyCustomException(MyCustomException ex) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage());
        logErrorDetails(ex);
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Object> handleIllegalStateException(IllegalStateException ex) {
        ApiError apiError = new ApiError(HttpStatus.CONFLICT, ex.getMessage());
        logErrorDetails(ex);
        return buildResponseEntity(apiError);
    }

   

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAll(Exception ex) {
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred");
        logErrorDetails(ex);
        return buildResponseEntity(apiError);
    }

    private void logErrorDetails(Throwable ex) {
        logger.error("Exception: ", ex);
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
    

    @ExceptionHandler(StackOverflowError.class)
    public ResponseEntity<Map<String, Object>> handleStackOverflowError(StackOverflowError ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "StackOverflowError");
        response.put("message", ex.getMessage());
        response.put("cause", ex.getCause());

        StackTraceElement[] stackTrace = ex.getStackTrace();
        StringBuilder stackTraceString = new StringBuilder();
        for (StackTraceElement element : stackTrace) {
            stackTraceString.append(element.toString()).append("\n");
        }
        response.put("stackTrace", stackTraceString.toString());

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
