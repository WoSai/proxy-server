package com.wosai.upay.proxy.exception;

public class RemoteResponse400 extends RemoteResponseAbnormal {
    private static final long serialVersionUID = 1L;

    public RemoteResponse400(String errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }

}
