package com.wukong.logisticsproject.controller;


import com.wukong.logisticsproject.ex.*;
import com.wukong.logisticsproject.vo.R;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({ServiceException.class, FileUploadException.class})
    public R handleException(Throwable e) {
        if (e instanceof InsertException) {
            return R.failure(R.State.ERR_INSERT_FAIL, e);
        } else if (e instanceof DeleteException) {
            return R.failure(R.State.ERR_DELETE_FAIL, e);
        } else if (e instanceof UpdateException) {
            return R.failure(R.State.ERR_UPDATE_FAIL, e);
        } else if (e instanceof ParameterValidationException) {
            return R.failure(R.State.ERR_PARAMETER_VALIDATION, e);

        } else if (e instanceof ValidationCodeException) {
            return R.failure(R.State.ERR_VALIDATIONCODE_VALIDATION, e);

        } else if (e instanceof ResetPasswordException) {
            return R.failure(R.State.ERR_RESETPASSWORD_FAIL, e);

        } else if (e instanceof NameExistException) {
            return R.failure(R.State.ERR_NAMEEXIST_FAIL, e);

        } else if (e instanceof loadingCarException) {
            return R.failure(R.State.ERR_LOADINGCAR_VALIDATION, e);

        } else if (e instanceof FileEmptyException) {
            return R.failure(R.State.ERR_UPLOAD_FILE_EMPTY, e);
        }else if (e instanceof FIleSizeException ) {
            return R.failure(R.State.ERR_UPLOAD_FILE_SIZE, e);
        }else if (e instanceof FileTypeException ) {
            return R.failure(R.State.ERR_UPLOAD_FILE_TYPE, e);
        }else if (e instanceof FileIOException ) {
            return R.failure(R.State.ERR_UPLOAD_IO, e);
        }else if (e instanceof FileStateException ) {
            return R.failure(R.State.ERR_UPLOAD_FILE_STATE, e);
        }else {
            return R.failure(R.State.ERR_UNKNOWN, e);
        }
    }
}
