package com.wosai.upay.proxy.exception;

public class ResponseResolveException extends Exception {
    private String code;

    private static final long serialVersionUID = 1L;

    public ResponseResolveException(String errorCode, String errorMessage) {
        super(errorMessage);
        this.code = errorCode;
    }

    public String getCode() {
        return code;
    }
}
