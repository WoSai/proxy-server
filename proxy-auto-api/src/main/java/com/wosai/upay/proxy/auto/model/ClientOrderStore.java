package com.wosai.upay.proxy.auto.model;

public enum ClientOrderStore {
    
    CLIENT_SN("client_sn",null),
    SN("sn",null),
    
	NAME("name",null),
	INDUSTRY("industry",null),
	LONGITUDE("longitude",null),
	LATITUDE("latitude",null),
	PROVINCE("province",null),
	CITY("city",null),
	STREET_ADDRESS("street_address",null),
	CONTACT_NAME("contact_name",null),
	CONTACT_PHONE("contact_phone",null),
	CONTACT_CELLPHONE("contact_cellphone",null),
	CONTACT_EMAIL("contact_email",null),
	CLIENT_MERCHANT_SN("client_merchant_sn",null),
	EXTRA("extra",null);
	
    
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
