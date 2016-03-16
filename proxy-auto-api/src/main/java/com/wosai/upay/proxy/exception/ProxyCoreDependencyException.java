package com.wosai.upay.proxy.exception;

public class ProxyCoreDependencyException extends ProxyAutoSystemException {

    private static final long serialVersionUID = 1L;

    public ProxyCoreDependencyException(String message, Throwable cause) {
        super(message, cause);
    }
    public ProxyCoreDependencyException(String message) {
        this(message, null);
    }
    public ProxyCoreDependencyException(Throwable cause) {
        this(null, cause);
    }

    @Override
    public String getCode() {
        return "PROXY_CORE_DEPENDENCY_EXCEPTION";
    }

}
