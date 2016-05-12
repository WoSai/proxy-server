package com.wosai.upay.proxy.upay.exception;

public class UpayApi400 extends UpayApiException {

    private static final long serialVersionUID = 1L;

    public UpayApi400(String message, Throwable cause) {
        super(message, cause);
    }
    public UpayApi400(String message) {
        this(message, null);
    }
    public UpayApi400(Throwable cause) {
        this(null, cause);
    }

    @Override
    public String getCode() {
        return "UPAY_API_400";
    }

}
