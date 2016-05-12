package com.wosai.upay.proxy.core.exception;

public class VendorApiBizError extends VendorApiException {

    private static final long serialVersionUID = 1L;

    public VendorApiBizError(String message, Throwable cause) {
        super(message, cause);
    }
    public VendorApiBizError(String message) {
        this(message, null);
    }
    public VendorApiBizError(Throwable cause) {
        this(null, cause);
    }

    @Override
    public String getCode() {
        return "VENDOR_API_BIZ_ERROR";
    }

}
