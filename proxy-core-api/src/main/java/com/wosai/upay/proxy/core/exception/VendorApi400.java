package com.wosai.upay.proxy.core.exception;

public class VendorApi400 extends VendorApiException {

    private static final long serialVersionUID = 1L;

    public VendorApi400(String message, Throwable cause) {
        super(message, cause);
    }
    public VendorApi400(String message) {
        this(message, null);
    }
    public VendorApi400(Throwable cause) {
        this(null, cause);
    }

    @Override
    public String getCode() {
        return "VENDOR_API_400";
    }

}
