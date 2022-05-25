package com.wukong.logisticsproject.ex;

/**
 * 上传文件大小超出了限制
 */
public class FIleSizeException extends FileUploadException {
    public FIleSizeException() {
    }

    public FIleSizeException(String message) {
        super(message);
    }

    public FIleSizeException(String message, Throwable cause) {
        super(message, cause);
    }

    public FIleSizeException(Throwable cause) {
        super(cause);
    }

    public FIleSizeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
