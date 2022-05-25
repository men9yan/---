package com.wukong.logisticsproject.ex;

/**
 * 修改密码错误
 */
public class ResetPasswordException extends ServiceException {
    public ResetPasswordException() {
    }

    public ResetPasswordException(String message) {
        super(message);
    }

    public ResetPasswordException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResetPasswordException(Throwable cause) {
        super(cause);
    }

    public ResetPasswordException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
