package com.wosai.upay.proxy.core.exception;

public abstract class ProxyCoreBizException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ProxyCoreBizException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProxyCoreBizException(String message) {
        this(message, null);
    }

    public ProxyCoreBizException(Throwable cause) {
        this(null, cause);
    }
    public abstract String getCode();

}
