package com.wosai.upay.proxy.exception;

public abstract class ProxyUpaySystemException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ProxyUpaySystemException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProxyUpaySystemException(String message) {
        this(message, null);
    }

    public ProxyUpaySystemException(Throwable cause) {
        this(null, cause);
    }
    public abstract String getCode();

}
