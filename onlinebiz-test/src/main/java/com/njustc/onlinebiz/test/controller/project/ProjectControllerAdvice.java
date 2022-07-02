package com.njustc.onlinebiz.test.controller.project;

import com.njustc.onlinebiz.test.exception.project.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**如果全部异常处理返回json，
 * 那么可以使用 @RestControllerAdvice 代替 @ControllerAdvice ，
 * 这样在方法上就可以不需要添加 @ResponseBody
 * 项目控制异常处理类
 *
 */
@Slf4j
@RestControllerAdvice
public class ProjectControllerAdvice {

    /**
     * 处理拒绝访问异常
     *
     * @param e e
     * @return {@link String}
     */@ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(ProjectPermissionDeniedException.class)
    public String handlePermissionDeniedException(ProjectPermissionDeniedException e) {
        log.warn("Permission Denied Exception: " + e.getMessage());
        e.printStackTrace();
        return e.getMessage();
    }


    /**
     * 处理dao failure异常
     *
     * @param e e
     * @return {@link String}
     */@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(ProjectDAOFailureException.class)
    public String handleDAOFailureException(ProjectDAOFailureException e) {
        log.error("DAO Failure Detected: " + e.getMessage());
        e.printStackTrace();
        return e.getMessage();
    }


    /**
     * 处理无效阶段异常
     *
     * @param e e
     * @return {@link String}
     */@ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ProjectInvalidStageException.class)
    public String handleInvalidStageException(ProjectInvalidStageException e) {
        log.warn("Invalid Stage Detected: " + e.getMessage());
        e.printStackTrace();
        return e.getMessage();
    }


    /**
     * 处理未找到异常
     *
     * @param e e
     * @return {@link String}
     */@ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ProjectNotFoundException.class)
    public String handleNotFoundException(ProjectNotFoundException e) {
        log.warn("Project Not Found: " + e.getMessage());
        e.printStackTrace();
        return e.getMessage();
    }


    /**
     * 处理无效参数异常
     *
     * @param e e
     * @return {@link String}
     */@ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ProjectInvalidArgumentException.class)
    public String handleInvalidArgumentException(ProjectInvalidArgumentException e) {
        log.warn("Invalid Argument encountered: " + e.getMessage());
        e.printStackTrace();
        return e.getMessage();
    }

    /**
     * 处理ioexception
     *
     * @param e e
     * @return {@link String}
     */@ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(IOException.class)
    public String handleIOException(IOException e) {
        log.warn("IO Exception: " + e.getMessage());
        e.printStackTrace();
        return e.getMessage();
    }
}
