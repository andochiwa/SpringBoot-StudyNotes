package com.github.admin.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author HAN
 * @version 1.0
 * @create 03-08-6:20
 */
@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "Too many users")
public class UserTooManyException extends RuntimeException {

    public UserTooManyException() {
    }

    public UserTooManyException(String message) {
        super(message);
    }
}
