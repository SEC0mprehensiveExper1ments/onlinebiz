package com.njustc.onlinebiz.test.exception.project;

public class ProjectPermissionDeniedException extends RuntimeException{
    public ProjectPermissionDeniedException(String message) {
        super(message);
    }
}
