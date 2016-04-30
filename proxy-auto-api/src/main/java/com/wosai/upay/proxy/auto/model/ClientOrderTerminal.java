package com.wosai.upay.proxy.auto.model;

public enum ClientOrderTerminal {

	DEVICE_ID("device_id",null),
	CLIENT_SN("client_sn",null),
    SN("sn",null),
    
	DEVICE_FINGERPRINT("device_fingerprint",null),
	NAME("name",null),
	TYPE("type",null),
	SDK_VERSION("sdk_version",null),
	OS_VERSION("os_version",null),
	LONGITUDE("longitude",null),
	LATITUDE("latitude",null),
	EXTRA("extra",null),
	TARGET("target",null),
	TARGET_TYPE("target_type",null),
	STORE_ID("store_id",null),
	VENDOR_APP_ID("vendor_app_id",null);
	

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
