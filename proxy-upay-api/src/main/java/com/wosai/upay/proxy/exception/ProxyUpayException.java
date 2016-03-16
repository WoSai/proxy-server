package com.wosai.upay.proxy.exception;

public abstract class ProxyUpayException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ProxyUpayException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProxyUpayException(String message) {
        this(message, null);
    }

    public ProxyUpayException(Throwable cause) {
        this(null, cause);
    }
    public abstract String getCode();

}
