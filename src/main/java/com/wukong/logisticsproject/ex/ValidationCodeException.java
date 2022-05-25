package com.wukong.logisticsproject.ex;

import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

public class ValidationCodeException extends ServiceException {


    public ValidationCodeException() {
        super();
    }

    public ValidationCodeException(String message) {
        super(message);
    }

    public ValidationCodeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidationCodeException(Throwable cause) {
        super(cause);
    }

    public ValidationCodeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}