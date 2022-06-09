package com.njustc.onlinebiz.contract.controller;

import com.njustc.onlinebiz.contract.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;

@Slf4j
@ControllerAdvice
public class ContractControllerAdvice {

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ContractNotFoundException.class)
    public String handleNotFoundException(ContractNotFoundException e) {
        log.warn("Not Found Exception: " + e.getMessage());
        return e.getMessage();
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(ContractPermissionDeniedException.class)
    public String handlePermissionDeniedException(ContractPermissionDeniedException e) {
        log.warn("Permission Denied: " + e.getMessage());
        return e.getMessage();
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ContractInvalidStageException.class)
    public String handleInvalidStageException(ContractInvalidStageException e) {
        log.warn("Invalid Stage Detected: " + e.getMessage());
        return e.getMessage();
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(ContractDAOFailureException.class)
    public String handleDAOFailureException(ContractDAOFailureException e) {
        log.error("DAO Failure Detected: " + e.getMessage());
        return e.getMessage();
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ContractCreateFailureException.class)
    public String handleCreateFailureException(ContractCreateFailureException e) {
        log.warn("Fail to Create Contract: " + e.getMessage());
        return e.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(InternalError.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleInternalError(InternalError error) {
        log.error("Internal Error: " + error.getMessage());
        error.printStackTrace();
        return error.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(IOException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleIOException(IOException e) {
        log.warn("IO Exception: " + e.getMessage());
        e.printStackTrace();
        return e.getMessage();
    }

}
