package com.wosai.upay.proxy.upay.exception;

public abstract class ProxyUpayClientException extends ProxyUpayException {
    private static final long serialVersionUID = 1L;

    public ProxyUpayClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProxyUpayClientException(String message) {
        this(message, null);
    }

    public ProxyUpayClientException(Throwable cause) {
        this(null, cause);
    }
    public abstract String getCode();

}
