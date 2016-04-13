package com.wosai.upay.proxy.auto.exception;

public abstract class ProxyAutoClientException extends ProxyAutoException {
    private static final long serialVersionUID = 1L;

    public ProxyAutoClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProxyAutoClientException(String message) {
        this(message, null);
    }

    public ProxyAutoClientException(Throwable cause) {
        this(null, cause);
    }
    public abstract String getCode();

}
