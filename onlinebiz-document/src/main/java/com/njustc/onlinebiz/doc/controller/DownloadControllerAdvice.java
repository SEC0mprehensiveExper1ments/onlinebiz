package com.njustc.onlinebiz.doc.controller;


import com.njustc.onlinebiz.doc.exception.DownloadDAOFailureException;
import com.njustc.onlinebiz.doc.exception.DownloadNotFoundException;
import com.njustc.onlinebiz.doc.exception.DownloadPermissionDeniedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class DownloadControllerAdvice {

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(DownloadNotFoundException.class)
    public String handleNotFoundException(DownloadNotFoundException e) {
        log.warn("Not Found Exception: " + e.getMessage());
        return e.getMessage();
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(DownloadDAOFailureException.class)
    public String handleDAOFailureException(DownloadDAOFailureException e) {
        log.warn("DAO Failure Detected: " + e.getMessage());
        return e.getMessage();
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(DownloadPermissionDeniedException.class)
    public String handlePermissionDeniedException(DownloadPermissionDeniedException e) {
        log.warn("DAO Failure Detected: " + e.getMessage());
        return e.getMessage();
    }

}
