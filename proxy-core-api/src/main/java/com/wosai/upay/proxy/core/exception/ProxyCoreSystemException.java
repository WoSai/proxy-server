package com.wosai.upay.proxy.core.exception;

public abstract class ProxyCoreSystemException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ProxyCoreSystemException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProxyCoreSystemException(String message) {
        this(message, null);
    }

    public ProxyCoreSystemException(Throwable cause) {
        this(null, cause);
    }
    public abstract String getCode();

}
