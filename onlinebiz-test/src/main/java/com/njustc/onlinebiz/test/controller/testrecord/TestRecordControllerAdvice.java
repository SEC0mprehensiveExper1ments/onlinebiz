package com.njustc.onlinebiz.test.controller.testrecord;

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

/**
 * 测试记录控制器建议
 *
 */
@Slf4j
@ControllerAdvice
public class TestRecordControllerAdvice {

    /**
     * 处理没有发现异常
     *
     * @param e e
     * @return {@link String}
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ReviewNotFoundException.class)
    public String handleNotFoundException(ReviewNotFoundException e) {
        log.warn("Not Found Exception: " + e.getMessage());
        return e.getMessage();
    }

    /**
     * 处理拒绝访问异常
     *
     * @param e e
     * @return {@link String}
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(ReviewPermissionDeniedException.class)
    public String handlePermissionDeniedException(ReviewPermissionDeniedException e){
        log.warn("Permission Denied: " + e.getMessage());
        return e.getMessage();
    }

    /**
     * 处理无效阶段异常
     *
     * @param e e
     * @return {@link String}
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ReviewInvalidStageException.class)
    public String handleInvalidStageException(ReviewInvalidStageException e) {
        log.warn("Invalid Stage Detected: " + e.getMessage());
        return e.getMessage();
    }

    /**
     * 处理daofailure异常
     *
     * @param e e
     * @return {@link String}
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(ReviewDAOFailureException.class)
    public String handleDAOFailureException(ReviewDAOFailureException e){
        log.warn("DAO Failure Detected: " + e.getMessage());
        return e.getMessage();
    }

    /**
     * 处理ioexception
     *
     * @param e e
     * @return {@link String}
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(IOException.class)
    public String handleIOException(IOException e) {
        log.warn("IO Exception: " + e.getMessage());
        return e.getMessage();
    }
}
