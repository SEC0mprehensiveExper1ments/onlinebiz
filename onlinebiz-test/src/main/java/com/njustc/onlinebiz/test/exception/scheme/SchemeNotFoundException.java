package com.njustc.onlinebiz.test.exception.scheme;

public class SchemeNotFoundException extends RuntimeException{
    public SchemeNotFoundException(String message) {
        super(message);
    }
}
