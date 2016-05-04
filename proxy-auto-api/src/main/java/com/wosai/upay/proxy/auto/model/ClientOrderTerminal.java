package com.wosai.upay.proxy.auto.model;

public enum ClientOrderTerminal {
	ID("id"),

	DEVICE_ID("device_id",null),
	CLIENT_SN("client_sn"),
    SN("sn",null),
    
	DEVICE_FINGERPRINT("device_fingerprint"),
	NAME("name"),
	TYPE("type"),
	SDK_VERSION("sdk_version"),
	OS_VERSION("os_version"),
	LONGITUDE("longitude"),
	LATITUDE("latitude"),
	EXTRA("extra"),
	TARGET("target"),
	TARGET_TYPE("target_type"),
	STORE_ID("store_id"),
	STORE_SN("store_sn"),
	CLIENT_STORE_SN("client_store_sn",null),
	VENDOR_APP_ID("vendor_app_appid");
	

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
