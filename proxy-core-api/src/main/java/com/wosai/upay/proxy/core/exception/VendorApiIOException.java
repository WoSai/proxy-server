package com.wosai.upay.proxy.core.exception;

public class VendorApiIOException extends VendorApiException {

    private static final long serialVersionUID = 1L;

    public VendorApiIOException(String message, Throwable cause) {
        super(message, cause);
    }
    public VendorApiIOException(String message) {
        this(message, null);
    }
    public VendorApiIOException(Throwable cause) {
        this(null, cause);
    }

    @Override
    public String getCode() {
        return "VENDOR_API_IO_EXCEPTION";
    }

}
