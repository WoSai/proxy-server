package com.wosai.upay.proxy.upay.exception;

public class UpayApiIOException extends UpayApiException {

    private static final long serialVersionUID = 1L;

    public UpayApiIOException(String message, Throwable cause) {
        super(message, cause);
    }
    public UpayApiIOException(String message) {
        this(message, null);
    }
    public UpayApiIOException(Throwable cause) {
        this(null, cause);
    }

    @Override
    public String getCode() {
        return "UPAY_API_IO_EXCEPTION";
    }

}
