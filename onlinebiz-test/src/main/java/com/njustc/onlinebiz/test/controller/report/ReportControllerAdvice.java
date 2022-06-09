package com.njustc.onlinebiz.test.controller.report;

import com.njustc.onlinebiz.test.exception.report.ReportDAOFailureException;
import com.njustc.onlinebiz.test.exception.report.ReportInvalidStageException;
import com.njustc.onlinebiz.test.exception.report.ReportNotFoundException;
import com.njustc.onlinebiz.test.exception.report.ReportPermissionDeniedException;
import com.njustc.onlinebiz.test.exception.review.ReviewDAOFailureException;
import com.njustc.onlinebiz.test.exception.review.ReviewInvalidStageException;
import com.njustc.onlinebiz.test.exception.review.ReviewNotFoundException;
import com.njustc.onlinebiz.test.exception.review.ReviewPermissionDeniedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;

@Slf4j
@ControllerAdvice
public class ReportControllerAdvice {
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ReportNotFoundException.class)
    public String handleNotFoundException(ReviewNotFoundException e) {
        log.warn("Not Found Exception: " + e.getMessage());
        return e.getMessage();
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(ReportPermissionDeniedException.class)
    public String handlePermissionDeniedException(ReviewPermissionDeniedException e) {
        log.warn("Permission Denied: " + e.getMessage());
        return e.getMessage();
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ReportInvalidStageException.class)
    public String handleInvalidStageException(ReviewInvalidStageException e) {
        log.warn("Invalid Stage Detected: " + e.getMessage());
        return e.getMessage();
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(ReportDAOFailureException.class)
    public String handleDAOFailureException(ReviewDAOFailureException e) {
        log.warn("DAO Failure Detected: " + e.getMessage());
        return e.getMessage();
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(IOException.class)
    public String handleIOException(IOException e) {
        log.warn("IO Exception: " + e.getMessage());
        return e.getMessage();
    }
}
