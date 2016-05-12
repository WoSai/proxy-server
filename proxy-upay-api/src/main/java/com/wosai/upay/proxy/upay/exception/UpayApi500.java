package com.wosai.upay.proxy.upay.exception;

public class UpayApi500 extends UpayApiException {

    private static final long serialVersionUID = 1L;

    public UpayApi500(String message, Throwable cause) {
        super(message, cause);
    }
    public UpayApi500(String message) {
        this(message, null);
    }
    public UpayApi500(Throwable cause) {
        this(null, cause);
    }

    @Override
    public String getCode() {
        return "UPAY_API_400";
    }

}
