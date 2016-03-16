package com.wosai.upay.proxy.exception;

public class UpayApiException extends ProxyUpaySystemException {

    private static final long serialVersionUID = 1L;

    public UpayApiException(String message, Throwable cause) {
        super(message, cause);
    }
    public UpayApiException(String message) {
        this(message, null);
    }
    public UpayApiException(Throwable cause) {
        this(null, cause);
    }

    @Override
    public String getCode() {
        return "VENDOR_API_EXCEPTION";
    }

}
