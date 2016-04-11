package com.wosai.upay.proxy.auto.model;

public enum ClientOrderRefund {

	TERMINAL_SN("terminal_sn"),

	SN("sn"),
	CLIENT_SN("client_sn"),
	REFUND_REQUEST_NO("refund_request_no"),
	OPERATOR("operator"),
	REFUND_AMOUNT("refund_amount");
    
    //本地接口字段名
	String value;
    //服务端接口字段名
    String map;
    
    private ClientOrderRefund(String value, String map) {
		this.value = value;
		this.map = map;
	}

	private ClientOrderRefund(String value) {
		this.value = value;
		this.map = value;
	}

	@Override
	public String toString() {
		
		return value;
	}

	public String getValue() {
		return value;
	}

	public String getMap() {
		return map;
	}
    
}
