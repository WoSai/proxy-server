package com.wosai.upay.proxy.exception;

public abstract class ProxyAutoBizException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ProxyAutoBizException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProxyAutoBizException(String message) {
        this(message, null);
    }

    public ProxyAutoBizException(Throwable cause) {
        this(null, cause);
    }
    public abstract String getCode();

}
