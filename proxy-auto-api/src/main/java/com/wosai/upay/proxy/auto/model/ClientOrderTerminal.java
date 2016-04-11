package com.wosai.upay.proxy.auto.model;

public enum ClientOrderTerminal {
	
	CLIENT_SN("client_sn",null),
    SN("sn",null);
	// TODO 待补充

    //本地接口字段名
	String value;
    //服务端接口字段名
    String map;
    
    private ClientOrderTerminal(String value, String map) {
		this.value = value;
		this.map = map;
	}

	private ClientOrderTerminal(String value) {
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
