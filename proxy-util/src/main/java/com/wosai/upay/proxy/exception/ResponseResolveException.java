package com.wosai.upay.proxy.exception;

public class ResponseResolveException extends Exception {
    
	private static final long serialVersionUID = 1L;

    public ResponseResolveException(String message, Throwable cause) {
        super(message, cause);
    }
    public ResponseResolveException(String message) {
        this(message, null);
    }
    public ResponseResolveException(Throwable cause) {
        this(null, cause);
    }
}
