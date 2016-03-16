package com.wosai.upay.proxy.exception;

public abstract class ProxyAutoException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ProxyAutoException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProxyAutoException(String message) {
        this(message, null);
    }

    public ProxyAutoException(Throwable cause) {
        this(null, cause);
    }
    public abstract String getCode();

}
