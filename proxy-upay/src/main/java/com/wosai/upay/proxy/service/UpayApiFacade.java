package com.wosai.upay.proxy.service;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;

import com.wosai.upay.httpclient.UpayHttpClient;
import com.wosai.upay.proxy.exception.UpayApiException;
import com.wosai.upay.proxy.model.Order;
import com.wosai.upay.validation.PropNotEmpty;

/**
 * 封装Vendor API客户端。vendorApiUrl/vendorSn/vendorKey 从配置文件读取 
 * @author dun
 *
 */
@Validated
public class UpayApiFacade {
    private String upayApiUrl;
    private String vendorSn;
    private String vendorKey;

    @Autowired
    private UpayHttpClient client;
    

    public String createStore(@PropNotEmpty.List({
                                @PropNotEmpty(Order.CLIENT_SN),
                                @PropNotEmpty(Order.TERMINAL_SN),
                              })
                              Map<String, Object> request) throws UpayApiException {
        try {
            String terminalSn = (String)request.get(Order.TERMINAL_SN);
            client.call(terminalSn, vendorKey, upayApiUrl, request);
            return null;
        }catch(IOException ex) {
            throw new UpayApiException("Failed to call upay api.", ex);
        }
    }
    
    public void setVendorApiUrl(String vendorApiUrl) {
        this.upayApiUrl = vendorApiUrl;
    }
    public void setVendorSn(String vendorSn) {
        this.vendorSn = vendorSn;
    }
    public void setVendorKey(String vendorKey) {
        this.vendorKey = vendorKey;
    }
}
