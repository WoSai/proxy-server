package com.wosai.upay.proxy.core.exception;

public abstract class ProxyCoreClientException extends ProxyCoreException {
    private static final long serialVersionUID = 1L;

    public ProxyCoreClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProxyCoreClientException(String message) {
        this(message, null);
    }

    public ProxyCoreClientException(Throwable cause) {
        this(null, cause);
    }
    public abstract String getCode();

}
