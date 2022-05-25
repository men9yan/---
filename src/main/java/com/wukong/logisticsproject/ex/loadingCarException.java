package com.wukong.logisticsproject.ex;


/**
 * 装车扫描的异常
 */
public class loadingCarException extends ServiceException {
    public loadingCarException() {
    }

    public loadingCarException(String message) {
        super(message);
    }

    public loadingCarException(String message, Throwable cause) {
        super(message, cause);
    }

    public loadingCarException(Throwable cause) {
        super(cause);
    }

    public loadingCarException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
