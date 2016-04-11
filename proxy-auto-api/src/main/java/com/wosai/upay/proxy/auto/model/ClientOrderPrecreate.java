package com.wosai.upay.proxy.auto.model;

public enum ClientOrderPrecreate {

	TERMINAL_SN("terminal_sn"),
	
	CLIENT_SN("client_sn"),
	TOTAL_AMOUNT("total_amount"),
	PAYWAY("payway"),
	SUB_PAYWAY("sub_payway"),
	PAYER_UID("payer_uid"),
	SUBJECT("subject"),
	OPERATOR("operator"),
	DESCRIPTION("description"),
	LONGITUDE("longitude"),
	LATITUDE("latitude"),
	EXTENDED("extended"),
	REFLECT("reflect");
    
    //本地接口字段名
	String value;
    //服务端接口字段名
    String map;
    
    private ClientOrderPrecreate(String value, String map) {
		this.value = value;
		this.map = map;
	}

	private ClientOrderPrecreate(String value) {
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
