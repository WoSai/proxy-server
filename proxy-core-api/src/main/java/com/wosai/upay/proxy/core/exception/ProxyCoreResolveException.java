package com.wosai.upay.proxy.core.exception;

public class ProxyCoreResolveException extends ProxyCoreSystemException {

    private static final long serialVersionUID = 1L;
    
    private String resultCode;
    
    private String errorCode;
    
    private String errorMessage;

    public ProxyCoreResolveException(String message, Throwable cause) {
        super(message, cause);
    }
    public ProxyCoreResolveException(String message) {
        this(message, null);
    }
    public ProxyCoreResolveException(Throwable cause) {
        this(null, cause);
    }
	public ProxyCoreResolveException(String resultCode, 
			String errorCode, String errorMessage) {
		super(errorMessage, null);
		this.resultCode = resultCode;
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

    @Override
    public String getCode() {
        return errorCode;
    }
	@Override
	public String getMessage() {
		return errorMessage;
	}
	public String getResultCode() {
		return resultCode;
	}
	public String getErrorCode() {
		return errorCode;
	}

}
