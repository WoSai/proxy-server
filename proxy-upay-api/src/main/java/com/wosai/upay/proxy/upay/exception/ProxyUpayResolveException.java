package com.wosai.upay.proxy.upay.exception;

/**
 * 转发服务端异常
 * @author qi
 *
 */
public class ProxyUpayResolveException extends ProxyUpaySystemException {

    private static final long serialVersionUID = 1L;
    
    private String resultCode;
    
    private String errorCode;
    
    private String errorMessage;

    public ProxyUpayResolveException(String message, Throwable cause) {
        super(message, cause);
    }
    public ProxyUpayResolveException(String message) {
        this(message, null);
    }
    public ProxyUpayResolveException(Throwable cause) {
        this(null, cause);
    }
	public ProxyUpayResolveException(String resultCode, 
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
