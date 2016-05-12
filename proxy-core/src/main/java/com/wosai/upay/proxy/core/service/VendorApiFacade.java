package com.wosai.upay.proxy.core.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;

import com.wosai.upay.httpclient.UpayHttpClient;
import com.wosai.upay.proxy.core.exception.VendorApi400;
import com.wosai.upay.proxy.core.exception.VendorApi500;
import com.wosai.upay.proxy.core.exception.VendorApiBizError;
import com.wosai.upay.proxy.core.exception.VendorApiException;
import com.wosai.upay.proxy.core.exception.VendorApiIOException;
import com.wosai.upay.proxy.core.model.Store;
import com.wosai.upay.proxy.core.model.Terminal;
import com.wosai.upay.proxy.exception.RemoteResponse400;
import com.wosai.upay.proxy.exception.RemoteResponse500;
import com.wosai.upay.proxy.exception.RemoteResponseBizError;
import com.wosai.upay.proxy.util.ResponseUtil;
import com.wosai.upay.validation.PropNotEmpty;

/**
 * 封装Vendor API客户端。vendorApiDomain/vendorSn/vendorKey 从配置文件读取 
 * @author dun
 *
 */
@Validated
public class VendorApiFacade {
	private String vendorAppId;

    private static final Logger logger = LoggerFactory.getLogger(VendorApiFacade.class);

    private String vendorSn;
    private String vendorKey;
    
    private String createStoreApiUrl;
    private String updateStoreApiUrl;
    private String getStoreApiUrl;
    private String createTerminalApiUrl;
    private String updateTerminalApiUrl;
    private String getTerminalApiUrl;
    private String moveTerminalApiUrl;
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
        
        return call2(createStoreApiUrl, request);
    }


	public Map<String, Object> updateStore(Map<String, Object> request) {
        return call2(updateStoreApiUrl, request);
	}


	public Map<String, Object> getStore(String storeId) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put(Store.ID, storeId);
        return call2(getStoreApiUrl, map);
	}
    

    public Map<String,Object> createTerminal(@PropNotEmpty.List({
        @PropNotEmpty(Terminal.CLIENT_SN),
        @PropNotEmpty(Terminal.NAME),
        @PropNotEmpty(Terminal.TYPE),
        @PropNotEmpty(Terminal.STORE_SN)
      })
                              Map<String, Object> request) throws VendorApiException {
    	request.put(Terminal.VENDOR_APP_ID, vendorAppId);
        return call2(createTerminalApiUrl, request);
    }


	public Map<String, Object> updateTerminal(Map<String, Object> request) {
		return call2(updateTerminalApiUrl, request);
	}


	public Map<String, Object> getTerminal(String terminalId) {

		Map<String,Object> request=new HashMap<String,Object>();
		request.put(Terminal.ID, terminalId);
		return call2(getTerminalApiUrl, request);
	}


	public Map<String, Object> moveTerminal(Map<String, Object> request) {
		return call2(moveTerminalApiUrl, request);
	}

	
	public Map<String,Object> activateTerminal(@PropNotEmpty.List({
        @PropNotEmpty(Terminal.CODE)
      })
                              Map<String, Object> request) throws VendorApiException {
	    
	    return call1(activateTerminalApiUrl, request);

	}
    
	private Map<String, Object> call1(String url, Map<String, Object> request) throws VendorApiException {
	    try {
            return ResponseUtil.resolve1(client.call(vendorSn, vendorKey, url, request));

	    } catch (RemoteResponse400 e) {

            logger.error(String.format("Vendor API %s specification might have changed. contact developer.", url), e);
            throw new VendorApi400(String.format("Vendor API 400 %s : %s", e.getCode(), e.getMessage()), e);

        } catch (RemoteResponse500 e) {

            logger.error(String.format("Vendor API %s service unavailable.", url), e);
            throw new VendorApi500(String.format("Vendor API 500 %s : %s", e.getCode(), e.getMessage()), e);

        } catch (IOException e) {

            logger.error(String.format("Vendor API %s failed on i/o.", url), e);
            throw new VendorApiIOException(String.format("Vendor API %s failed on i/o.", url), e);

        }
	}

    private Map<String, Object> call2(String url, Map<String, Object> request) throws VendorApiException {
        try {
            return ResponseUtil.resolve2(client.call(vendorSn, vendorKey, url, request));

        } catch (RemoteResponse400 e) {
            logger.error(String.format("Vendor API %s specification might have changed. contact developer.", url), e);
            throw new VendorApi400(String.format("Vendor API 400 %s : %s", e.getCode(), e.getMessage()), e);

        } catch (RemoteResponse500 e) {
            logger.error(String.format("Vendor API %s service unavailable.", url), e);
            throw new VendorApi500(String.format("Vendor API 500 %s : %s", e.getCode(), e.getMessage()), e);

        } catch (RemoteResponseBizError e) {
            logger.warn(String.format("Vendor API %s returns biz error", url), e);
            throw new VendorApiBizError(String.format("Vendor API BizError %s : %s", e.getCode(), e.getMessage()), e);

        } catch (IOException e) {
            logger.error(String.format("Vendor API %s failed on i/o.", url), e);
            throw new VendorApiIOException(String.format("Vendor API %s failed on i/o.", url), e);

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


	public void setVendorAppId(String vendorAppId) {
		this.vendorAppId = vendorAppId;
	}


	public void setMoveTerminalApiUrl(String moveTerminalApiUrl) {
		this.moveTerminalApiUrl = moveTerminalApiUrl;
	}
    
}
