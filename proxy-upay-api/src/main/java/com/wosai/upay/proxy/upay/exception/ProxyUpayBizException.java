package com.wosai.upay.proxy.upay.exception;

public abstract class ProxyUpayBizException extends ProxyUpayException {
    private static final long serialVersionUID = 1L;

    public ProxyUpayBizException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProxyUpayBizException(String message) {
        this(message, null);
    }

    public ProxyUpayBizException(Throwable cause) {
        this(null, cause);
    }
    public abstract String getCode();

}
