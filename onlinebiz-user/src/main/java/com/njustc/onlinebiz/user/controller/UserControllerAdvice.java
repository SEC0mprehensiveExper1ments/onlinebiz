package com.njustc.onlinebiz.user.controller;

import com.njustc.onlinebiz.user.exception.UserDBFailureException;
import com.njustc.onlinebiz.user.exception.UserInvalidArgumentException;
import com.njustc.onlinebiz.user.exception.UserPermissonDeniedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class UserControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserInvalidArgumentException.class)
    public String handleInvalidArgument(UserInvalidArgumentException e) {
        log.warn("Invalid Argument: " + e.getMessage());
        return e.getMessage();
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(UserPermissonDeniedException.class)
    public String handlePermissionDenied(UserPermissonDeniedException e) {
        log.warn("Permission Denied: " + e.getMessage());
        return e.getMessage();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(UserDBFailureException.class)
    public String handleDBFailure(UserDBFailureException e) {
        log.error("DB Failure: " + e.getMessage());
        return e.getMessage();
    }

}
