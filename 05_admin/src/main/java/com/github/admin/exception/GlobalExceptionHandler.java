package com.github.admin.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 处理整个web的异常
 * @author HAN
 * @version 1.0
 * @create 03-08-6:07
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler({ArithmeticException.class, NullPointerException.class}) // 处理哪些异常
    public String handleArithmeticException(Exception e) {
        return "login";
    }

}
