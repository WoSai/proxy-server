package com.wosai.upay.proxy.auto.model;

public enum ClientOrderRevoke {

	TERMINAL_SN("terminal_sn"),

	SN("sn"),
	CLIENT_SN("client_sn");
    
    //本地接口字段名
	String value;
    //服务端接口字段名
    String map;
    
    private ClientOrderRevoke(String value, String map) {
		this.value = value;
		this.map = map;
	}

	private ClientOrderRevoke(String value) {
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
