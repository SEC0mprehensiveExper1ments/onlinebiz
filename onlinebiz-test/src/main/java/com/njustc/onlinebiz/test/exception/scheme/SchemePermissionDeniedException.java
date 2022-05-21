package com.njustc.onlinebiz.test.exception.scheme;

public class SchemePermissionDeniedException extends RuntimeException{
    public SchemePermissionDeniedException(String message) {
        super(message);
    }
}
