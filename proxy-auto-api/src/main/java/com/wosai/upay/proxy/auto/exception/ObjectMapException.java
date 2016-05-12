package com.wosai.upay.proxy.auto.exception;

public class ObjectMapException extends ProxyAutoSystemException {

    private static final long serialVersionUID = 1L;

    public ObjectMapException(String message, Throwable cause) {
        super(message, cause);
    }
    public ObjectMapException(String message) {
        this(message, null);
    }
    public ObjectMapException(Throwable cause) {
        this(null, cause);
    }

    @Override
    public String getCode() {
        return "OBJECT_MAP_EXCEPTION";
    }

}
