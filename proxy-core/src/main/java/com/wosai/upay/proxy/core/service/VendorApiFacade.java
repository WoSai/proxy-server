package com.wosai.upay.proxy.core.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;

import com.wosai.upay.httpclient.UpayHttpClient;
import com.wosai.upay.proxy.core.exception.VendorApiException;
import com.wosai.upay.proxy.core.model.Store;
import com.wosai.upay.proxy.core.model.Terminal;
import com.wosai.upay.proxy.exception.BizResponseResolveException;
import com.wosai.upay.proxy.exception.ResponseResolveException;
import com.wosai.upay.proxy.util.ResponseUtil;
import com.wosai.upay.validation.PropNotEmpty;

/**
 * 封装Vendor API客户端。vendorApiDomain/vendorSn/vendorKey 从配置文件读取 
 * @author dun
 *
 */
@Validated
public class VendorApiFacade {
    private String vendorSn;
    private String vendorKey;
    
    private String createStoreApiUrl;
    private String updateStoreApiUrl;
    private String getStoreApiUrl;
    private String createTerminalApiUrl;
    private String updateTerminalApiUrl;
    private String getTerminalApiUrl;
    private String activateTerminalApiUrl;

    @Autowired
    private UpayHttpClient client;
    

    public Map<String,Object> createStore(@PropNotEmpty.List({
						        @PropNotEmpty(Store.CLIENT_SN),
						        @PropNotEmpty(Store.NAME),
						        @PropNotEmpty(Store.MERCHANT_ID),
						        @PropNotEmpty(Store.PROVINCE),
						        @PropNotEmpty(Store.CITY),
						        @PropNotEmpty(Store.DISTRICT),
						        @PropNotEmpty(Store.STREET_ADDRESS),
						        @PropNotEmpty(Store.CONTACT_NAME),
						        @PropNotEmpty(Store.CONTACT_CELLPHONE)
						      })
                              Map<String, Object> request) throws VendorApiException {
        try {
        	String url = createStoreApiUrl;
            return resolve2(client.call(vendorSn, vendorKey, url, request));
        }catch(IOException ex) {
            throw new VendorApiException("Failed to call createStore api.", ex);
        }
    }


	public void updateStore(Map<String, Object> request) {
		try {
        	String url = updateStoreApiUrl;
            resolve2(client.call(vendorSn, vendorKey, url, request));
        }catch(IOException ex) {
            throw new VendorApiException("Failed to call updateStore api.", ex);
        }
	}


	public Map<String, Object> getStore(String sn) {
		try {
        	String url = getStoreApiUrl;
            return resolve2(client.call(vendorSn, vendorKey, url, null));
        }catch(IOException ex) {
            throw new VendorApiException("Failed to call getStore api.", ex);
        }
	}
    

    public Map<String,Object> createTerminal(@PropNotEmpty.List({
        @PropNotEmpty(Terminal.CLIENT_SN),
        @PropNotEmpty(Terminal.STORE_SN),
        @PropNotEmpty(Terminal.NAME),
        @PropNotEmpty(Terminal.TYPE),
        @PropNotEmpty(Terminal.STORE_ID)
      })
                              Map<String, Object> request) throws VendorApiException {
    	try {
        	String url = createTerminalApiUrl;
            return resolve2(client.call(vendorSn, vendorKey, url, request));
        }catch(IOException ex) {
            throw new VendorApiException("Failed to call createTerminal api.", ex);
        }
    }


	public void updateTerminal(Map<String, Object> request) {
		try {
        	String url = updateTerminalApiUrl;
            resolve2(client.call(vendorSn, vendorKey, url, request));
        }catch(IOException ex) {
            throw new VendorApiException("Failed to call updateTerminal api.", ex);
        }
	}


	public Map<String, Object> getTerminal(String sn) {
		try {
			Map<String,Object> map=new HashMap<String,Object>();
        	String url = getTerminalApiUrl;
            return resolve2(client.call(vendorSn, vendorKey, url, map));
        }catch(IOException ex) {
            throw new VendorApiException("Failed to call getTerminal api.", ex);
        }
	}

	
	public Map<String,Object> activateTerminal(@PropNotEmpty.List({
        @PropNotEmpty(Terminal.CODE)
      })
                              Map<String, Object> request) throws VendorApiException {
    	try {
        	String url = activateTerminalApiUrl;
            return resolve1(client.call(vendorSn, vendorKey, url, request));
        }catch(IOException ex) {
            throw new VendorApiException("Failed to call activateTerminal api.", ex);
        }
    }
    
    private Map<String, Object> resolve1(Map<String, Object> result) {
        try {
            return ResponseUtil.resolve1(result);

        } catch (ResponseResolveException e) {
            throw new VendorApiException(String.format("Vendor API response has error - %s : %s", e.getCode(), e.getMessage()));

        }
    }

    private Map<String, Object> resolve2(Map<String, Object> result) {
        try {
            return ResponseUtil.resolve2(result);

        } catch (ResponseResolveException e) {
            throw new VendorApiException(String.format("Vendor API response has error - %s : %s", e.getCode(), e.getMessage()));

        } catch (BizResponseResolveException e) {
            throw new VendorApiException(String.format("Vendor API biz response has error - %s : %s", e.getCode(), e.getMessage()));

        }
    }

    public void setVendorSn(String vendorSn) {
        this.vendorSn = vendorSn;
    }
    public void setVendorKey(String vendorKey) {
        this.vendorKey = vendorKey;
    }


	public void setCreateStoreApiUrl(String createStoreApiUrl) {
		this.createStoreApiUrl = createStoreApiUrl;
	}


	public void setUpdateStoreApiUrl(String updateStoreApiUrl) {
		this.updateStoreApiUrl = updateStoreApiUrl;
	}


	public void setGetStoreApiUrl(String getStoreApiUrl) {
		this.getStoreApiUrl = getStoreApiUrl;
	}


	public void setCreateTerminalApiUrl(String createTerminalApiUrl) {
		this.createTerminalApiUrl = createTerminalApiUrl;
	}


	public void setUpdateTerminalApiUrl(String updateTerminalApiUrl) {
		this.updateTerminalApiUrl = updateTerminalApiUrl;
	}


	public void setGetTerminalApiUrl(String getTerminalApiUrl) {
		this.getTerminalApiUrl = getTerminalApiUrl;
	}


	public void setActivateTerminalApiUrl(String activateTerminalApiUrl) {
		this.activateTerminalApiUrl = activateTerminalApiUrl;
	}
    
}
