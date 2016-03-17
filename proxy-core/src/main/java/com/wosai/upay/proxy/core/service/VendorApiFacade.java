package com.wosai.upay.proxy.core.service;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.wosai.upay.httpclient.UpayHttpClient;
import com.wosai.upay.proxy.core.exception.VendorApiException;
import com.wosai.upay.proxy.core.model.Store;
import com.wosai.upay.validation.PropNotEmpty;

/**
 * 封装Vendor API客户端。vendorApiUrl/vendorSn/vendorKey 从配置文件读取 
 * @author dun
 *
 */
@Validated
public class VendorApiFacade {
    private String vendorApiUrl;
    private String vendorSn;
    private String vendorKey;

    @Autowired
    private UpayHttpClient client;
    

    public String createStore(@PropNotEmpty.List({
                                @PropNotEmpty(Store.CLIENT_SN),
                                @PropNotEmpty(Store.NAME),
                                @PropNotEmpty(Store.MERCHANT_SN)
                              })
                              Map<String, Object> request) throws VendorApiException {
        try {
            client.call(vendorSn, vendorKey, vendorApiUrl + "/store/create", request);
            return null;
        }catch(IOException ex) {
            throw new VendorApiException("Failed to call vendor api.", ex);
        }
    }
    
    public void setVendorApiUrl(String vendorApiUrl) {
        this.vendorApiUrl = vendorApiUrl;
    }
    public void setVendorSn(String vendorSn) {
        this.vendorSn = vendorSn;
    }
    public void setVendorKey(String vendorKey) {
        this.vendorKey = vendorKey;
    }
}
