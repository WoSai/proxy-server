package com.wosai.upay.proxy.exception;

public class RemoteResponse500 extends RemoteResponseAbnormal {
    private static final long serialVersionUID = 1L;

    public RemoteResponse500(String errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }

}
