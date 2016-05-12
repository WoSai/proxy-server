package com.wosai.upay.proxy.exception;

public abstract class RemoteResponseAbnormal extends RemoteResponseError {
    private static final long serialVersionUID = 1L;

    public RemoteResponseAbnormal(String errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }

}
