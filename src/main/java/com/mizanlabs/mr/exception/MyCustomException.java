package com.mizanlabs.mr.exception;
public class MyCustomException extends RuntimeException {
    public MyCustomException(String message, Throwable cause) {
        super(message, cause);
    }

}