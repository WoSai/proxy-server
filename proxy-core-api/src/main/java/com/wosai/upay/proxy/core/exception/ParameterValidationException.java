package com.wosai.upay.proxy.core.exception;

public class ParameterValidationException extends ProxyCoreClientException {
    private static final long serialVersionUID = 1L;

    public ParameterValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParameterValidationException(String message) {
        this(message, null);
    }

    public ParameterValidationException(Throwable cause) {
        this(null, cause);
    }
    @Override
    public String getCode() {
        return "INVALID_PARAMS";
    }

}
