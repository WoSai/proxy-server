package com.wosai.upay.proxy.core.exception;

public abstract class ProxyCoreException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ProxyCoreException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProxyCoreException(String message) {
        this(message, null);
    }

    public ProxyCoreException(Throwable cause) {
        this(null, cause);
    }
    public abstract String getCode();

}
