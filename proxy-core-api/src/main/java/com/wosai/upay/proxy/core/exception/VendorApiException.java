package com.wosai.upay.proxy.core.exception;

public abstract class VendorApiException extends ProxyCoreSystemException {

    private static final long serialVersionUID = 1L;

    public VendorApiException(String message, Throwable cause) {
        super(message, cause);
    }
    public VendorApiException(String message) {
        this(message, null);
    }
    public VendorApiException(Throwable cause) {
        this(null, cause);
    }


}
