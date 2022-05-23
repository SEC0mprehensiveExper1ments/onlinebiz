package com.njustc.onlinebiz.test.controller.project;

import com.njustc.onlinebiz.test.exception.project.ProjectDAOFailureException;
import com.njustc.onlinebiz.test.exception.project.ProjectInvalidStageException;
import com.njustc.onlinebiz.test.exception.project.ProjectNotFoundException;
import com.njustc.onlinebiz.test.exception.project.ProjectPermissionDeniedException;
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
    @ExceptionHandler(ProjectPermissionDeniedException.class)
    public String handlePermissionDeniedException(ProjectPermissionDeniedException e) {
        log.warn("Permission Denied Exception: " + e.getMessage());
        return e.getMessage();
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(ProjectDAOFailureException.class)
    public String handleDAOFailureException(ProjectDAOFailureException e) {
        log.error("DAO Failure Detected: " + e.getMessage());
        return e.getMessage();
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ProjectInvalidStageException.class)
    public String handleInvalidStageException(ProjectInvalidStageException e) {
        log.warn("Invalid Stage Detected: " + e.getMessage());
        return e.getMessage();
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ProjectNotFoundException.class)
    public String handleNotFoundException(ProjectNotFoundException e) {
        log.warn("Project Not Found: " + e.getMessage());
        return e.getMessage();
    }
}
