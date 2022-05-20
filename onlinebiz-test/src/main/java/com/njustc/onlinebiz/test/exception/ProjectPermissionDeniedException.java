package com.njustc.onlinebiz.test.exception;

public class ProjectPermissionDeniedException extends RuntimeException{
    public ProjectPermissionDeniedException(String message) {
        super(message);
    }
}
