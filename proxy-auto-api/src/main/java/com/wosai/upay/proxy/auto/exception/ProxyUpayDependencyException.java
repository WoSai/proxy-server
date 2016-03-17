package com.wosai.upay.proxy.auto.exception;

public class ProxyUpayDependencyException extends ProxyAutoSystemException {

    private static final long serialVersionUID = 1L;

    public ProxyUpayDependencyException(String message, Throwable cause) {
        super(message, cause);
    }
    public ProxyUpayDependencyException(String message) {
        this(message, null);
    }
    public ProxyUpayDependencyException(Throwable cause) {
        this(null, cause);
    }

    @Override
    public String getCode() {
        return "PROXY_UPAY_DEPENDENCY_EXCEPTION";
    }

}
