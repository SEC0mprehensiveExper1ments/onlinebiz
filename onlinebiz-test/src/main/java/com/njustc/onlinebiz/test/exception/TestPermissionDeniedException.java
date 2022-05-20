package com.njustc.onlinebiz.test.exception;

public class TestPermissionDeniedException extends RuntimeException{
    public TestPermissionDeniedException(String message) {
        super(message);
    }
}
