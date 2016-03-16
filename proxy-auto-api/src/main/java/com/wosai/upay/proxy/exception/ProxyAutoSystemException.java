package com.wosai.upay.proxy.exception;

public abstract class ProxyAutoSystemException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ProxyAutoSystemException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProxyAutoSystemException(String message) {
        this(message, null);
    }

    public ProxyAutoSystemException(Throwable cause) {
        this(null, cause);
    }
    public abstract String getCode();

}
