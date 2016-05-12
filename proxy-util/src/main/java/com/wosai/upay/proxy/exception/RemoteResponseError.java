package com.wosai.upay.proxy.exception;

public abstract class RemoteResponseError extends Exception {
    private String code;

    private static final long serialVersionUID = 1L;

    public RemoteResponseError(String errorCode, String errorMessage) {
        super(errorMessage);
        this.code = errorCode;
    }

    public String getCode() {
        return code;
    }
}
