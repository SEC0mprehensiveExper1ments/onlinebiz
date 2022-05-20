package com.njustc.onlinebiz.test.controller;

import com.njustc.onlinebiz.test.exception.TestDAOFailureException;
import com.njustc.onlinebiz.test.exception.TestInvalidStageException;
import com.njustc.onlinebiz.test.exception.TestNotFoundException;
import com.njustc.onlinebiz.test.exception.TestPermissionDeniedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class ProjectControllerAdvice {
    @ResponseBody
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(TestPermissionDeniedException.class)
    public String handlePermissionDeniedException(TestPermissionDeniedException e) {
        log.warn("Permission Denied Exception: " + e.getMessage());
        return e.getMessage();
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(TestDAOFailureException.class)
    public String handleDAOFailureException(TestDAOFailureException e) {
        log.error("DAO Failure Detected: " + e.getMessage());
        return e.getMessage();
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(TestInvalidStageException.class)
    public String handleInvalidStageException(TestInvalidStageException e) {
        log.warn("Invalid Stage Detected: " + e.getMessage());
        return e.getMessage();
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(TestNotFoundException.class)
    public String handleNotFoundException(TestNotFoundException e) {
        log.warn("Test Not Found: " + e.getMessage());
        return e.getMessage();
    }
}
