package com.njustc.onlinebiz.test.exception.scheme;

public class SchemeDAOFailureException extends RuntimeException{
    public SchemeDAOFailureException(String message) {
        super(message);
    }
}
