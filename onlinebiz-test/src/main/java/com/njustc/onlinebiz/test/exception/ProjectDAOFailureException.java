package com.njustc.onlinebiz.test.exception;

public class ProjectDAOFailureException extends RuntimeException{
    public ProjectDAOFailureException(String message) {
        super(message);
    }
}
