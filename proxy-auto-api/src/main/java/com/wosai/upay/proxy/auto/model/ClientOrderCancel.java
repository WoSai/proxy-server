package com.wosai.upay.proxy.auto.model;

public enum ClientOrderCancel {

	TERMINAL_SN("terminal_sn"),

	SN("sn"),
	CLIENT_SN("client_sn");
    
    //本地接口字段名
	String value;
    //服务端接口字段名
    String map;
    
    private ClientOrderCancel(String value, String map) {
		this.value = value;
		this.map = map;
	}

	private ClientOrderCancel(String value) {
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
