package com.wukong.logisticsproject.ex;

/**
 * 名称已存在异常
 */
public class NameExistException extends ServiceException {
    public NameExistException() {
    }

    public NameExistException(String message) {
        super(message);
    }

    public NameExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public NameExistException(Throwable cause) {
        super(cause);
    }

    public NameExistException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
