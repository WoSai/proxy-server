package com.wosai.upay.proxy.exception;

public class BizResponseResolveException extends Exception {
    private String code;

    private static final long serialVersionUID = 1L;

    public BizResponseResolveException(String errorCode, String errorMessage) {
        super(errorMessage);
        this.code = errorCode;
    }

    public String getCode() {
        return code;
    }
}
