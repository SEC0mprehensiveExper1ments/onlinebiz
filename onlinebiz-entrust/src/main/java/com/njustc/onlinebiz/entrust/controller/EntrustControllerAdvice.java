package com.njustc.onlinebiz.entrust.controller;

import com.njustc.onlinebiz.entrust.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestControllerAdvice
public class EntrustControllerAdvice {

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(EntrustPermissionDeniedException.class)
    public String handlePermissionDeniedException(EntrustPermissionDeniedException e) {
        log.warn("Permission Denied Exception: " + e.getMessage());
        return e.getMessage();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(EntrustDAOFailureException.class)
    public String handleDAOFailureException(EntrustDAOFailureException e) {
        log.error("DAO Failure Detected: " + e.getMessage());
        return e.getMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EntrustInvalidStageException.class)
    public String handleInvalidStageException(EntrustInvalidStageException e) {
        log.warn("Invalid Stage Detected: " + e.getMessage());
        return e.getMessage();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntrustNotFoundException.class)
    public String handleNotFoundException(EntrustNotFoundException e) {
        log.warn("Entrust Not Found: " + e.getMessage());
        return e.getMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EntrustInvalidArgumentException.class)
    public String handleInvalidArgumentException(EntrustInvalidArgumentException e) {
        log.warn("Invalid Argument encountered: " + e.getMessage());
        return e.getMessage();
    }

}
