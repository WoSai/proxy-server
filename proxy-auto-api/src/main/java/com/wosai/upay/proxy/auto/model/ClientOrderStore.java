package com.wosai.upay.proxy.auto.model;

public enum ClientOrderStore {
	ID("id"),
    
    CLIENT_SN("client_sn"),
    SN("sn",null),
    
	NAME("name"),
	INDUSTRY("industry"),
	LONGITUDE("longitude"),
	LATITUDE("latitude"),
	PROVINCE("province"),
	DISTRICT("district"),
	CITY("city"),
	STREET_ADDRESS("street_address"),
	CONTACT_NAME("contact_name"),
	CONTACT_PHONE("contact_phone"),
	CONTACT_CELLPHONE("contact_cellphone"),
	CONTACT_EMAIL("contact_email"),
	MERCHANT_SN("merchant_sn",null),
	CLIENT_MERCHANT_SN("client_merchant_sn",null),
	MERCHANT_ID("merchant_id"),
	EXTRA("extra");
	
    
    //本地接口字段名
	String value;
    //服务端接口字段名
    String map;
    
    private ClientOrderStore(String value, String map) {
		this.value = value;
		this.map = map;
	}

	private ClientOrderStore(String value) {
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
