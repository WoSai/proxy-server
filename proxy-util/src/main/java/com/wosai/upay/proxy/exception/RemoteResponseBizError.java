package com.wosai.upay.proxy.exception;

public class RemoteResponseBizError extends RemoteResponseError {
    private static final long serialVersionUID = 1L;

    public RemoteResponseBizError(String errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }

}
