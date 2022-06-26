package com.njustc.onlinebiz.sample.controller;

import com.njustc.onlinebiz.sample.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class SampleControllerAdvice {

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(SampleNotFoundException.class)
    public String handleNotFoundException(SampleNotFoundException e) {
        log.warn("Not Found Exception: " + e.getMessage());
        return e.getMessage();
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(SamplePermissionDeniedException.class)
    public String handlePermissionDeniedException(SamplePermissionDeniedException e) {
        log.warn("Permission Denied: " + e.getMessage());
        return e.getMessage();
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(SampleInvalidStageException.class)
    public String handleInvalidStageException(SampleInvalidStageException e) {
        log.warn("Invalid Stage Detected: " + e.getMessage());
        return e.getMessage();
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(SampleDAOFailureException.class)
    public String handleDAOFailureException(SampleDAOFailureException e) {
        log.warn("DAO Failure Detected: " + e.getMessage());
        return e.getMessage();
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(SampleInvalidArgumentException.class)
    public String handleInvalidArgumentException(SampleInvalidArgumentException e) {
        log.warn("Invalid Argument Detected: " + e.getMessage());
        return e.getMessage();
    }
}
