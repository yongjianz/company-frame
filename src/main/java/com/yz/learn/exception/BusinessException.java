package com.yz.learn.exception;

import com.yz.learn.exception.code.BaseResponseCode;
import lombok.Data;



public class BusinessException extends RuntimeException {

    private final int code;
    private final String defaultMessage;

    public BusinessException(int code, String defaultMessage) {
        super(defaultMessage);
        this.code = code;
        this.defaultMessage = defaultMessage;
    }

    public BusinessException(BaseResponseCode baseResponseCode) {
       this(baseResponseCode.getCode(), baseResponseCode.getMsg());
    }

    public int getCode() {
        return code;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }
}
