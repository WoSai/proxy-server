package com.wosai.upay.proxy.auto.exception;

public class UnknownClientMerchantSnException extends ProxyAutoClientException {
    private static final long serialVersionUID = 1L;


    public UnknownClientMerchantSnException(String message) {
        super(message);
    }

    @Override
    public String getCode() {
        return "UNKNOWN_CLIENT_MERCHANT_SN";
    }

}
