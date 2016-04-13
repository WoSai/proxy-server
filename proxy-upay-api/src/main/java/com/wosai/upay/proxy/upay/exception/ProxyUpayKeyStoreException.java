package com.wosai.upay.proxy.upay.exception;

public class ProxyUpayKeyStoreException extends ProxyUpaySystemException {

    private static final long serialVersionUID = 1L;

    public ProxyUpayKeyStoreException(String message, Throwable cause) {
        super(message, cause);
    }
    public ProxyUpayKeyStoreException(String message) {
        this(message, null);
    }
    public ProxyUpayKeyStoreException(Throwable cause) {
        this(null, cause);
    }

    @Override
    public String getCode() {
        return "KEY_STORE_EXCEPTION";
    }

}
