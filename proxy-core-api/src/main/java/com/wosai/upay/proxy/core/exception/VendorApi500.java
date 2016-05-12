package com.wosai.upay.proxy.core.exception;

public class VendorApi500 extends VendorApiException {

    private static final long serialVersionUID = 1L;

    public VendorApi500(String message, Throwable cause) {
        super(message, cause);
    }
    public VendorApi500(String message) {
        this(message, null);
    }
    public VendorApi500(Throwable cause) {
        this(null, cause);
    }

    @Override
    public String getCode() {
        return "VENDOR_API_500";
    }

}
