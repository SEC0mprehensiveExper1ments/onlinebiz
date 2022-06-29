package com.njustc.onlinebiz.test.controller.scheme;

import com.njustc.onlinebiz.test.exception.scheme.SchemeDAOFailureException;
import com.njustc.onlinebiz.test.exception.scheme.SchemeInvalidStageException;
import com.njustc.onlinebiz.test.exception.scheme.SchemeNotFoundException;
import com.njustc.onlinebiz.test.exception.scheme.SchemePermissionDeniedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 测试方案控制器方案建议
 *
 */
@Slf4j
@ControllerAdvice
public class SchemeControllerAdvice {
    /**
     * 处理拒绝访问异常
     *
     * @param e e
     * @return {@link String}
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(SchemePermissionDeniedException.class)
    public String handlePermissionDeniedException(SchemePermissionDeniedException e) {
        log.warn("Permission Denied Exception: " + e.getMessage());
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
    @ExceptionHandler(SchemeDAOFailureException.class)
    public String handleDAOFailureException(SchemeDAOFailureException e) {
        log.error("DAO Failure Detected: " + e.getMessage());
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
    @ExceptionHandler(SchemeInvalidStageException.class)
    public String handleInvalidStageException(SchemeInvalidStageException e) {
        log.warn("Invalid Stage Detected: " + e.getMessage());
        return e.getMessage();
    }

    /**
     * 处理没有发现异常
     *
     * @param e e
     * @return {@link String}
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(SchemeNotFoundException.class)
    public String handleNotFoundException(SchemeNotFoundException e) {
        log.warn("Scheme Not Found: " + e.getMessage());
        return e.getMessage();
    }
}
