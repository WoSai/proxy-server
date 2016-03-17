package com.wosai.upay.proxy.upay.service;

import java.io.IOException;
import java.util.Map;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;

import com.wosai.upay.httpclient.UpayHttpClient;
import com.wosai.upay.proxy.upay.exception.UpayApiException;
import com.wosai.upay.proxy.upay.model.Order;
import com.wosai.upay.validation.PropNotEmpty;

/**
 * 封装支付网关API 
 * @author dun
 *
 */
@Validated
public class UpayApiFacade {

    private String upayApiUrl;

    @Autowired
    private UpayHttpClient client;
    

    public Map<String, Object> pay(@NotEmpty(message="终端号不能为空")
                                   String terminalSn,
                                   @NotEmpty(message="终端密钥不能为空")
                                   String terminalKey,
                                   @PropNotEmpty.List({
                                       @PropNotEmpty(Order.CLIENT_SN),
                                   })
                                   Map<String, Object> request) throws UpayApiException {

        try {
            // 直接返回支付网关的结果
            return client.call(terminalSn, terminalKey, upayApiUrl + "/pay", request);
        }catch(IOException ex) {
            throw new UpayApiException("Failed to call upay api.", ex);
        }
    }
    
    public Map<String, Object> precreate(@NotEmpty(message="终端号不能为空")
                                         String terminalSn,
                                         @NotEmpty(message="终端密钥不能为空")
                                         String terminalKey,
                                         @PropNotEmpty.List({
                                             @PropNotEmpty(Order.CLIENT_SN),
                                         })
                                         Map<String, Object> request) throws UpayApiException {

        try {
            // 直接返回支付网关的结果
            return client.call(terminalSn, terminalKey, upayApiUrl + "/precreate", request);
        }catch(IOException ex) {
            throw new UpayApiException("Failed to call upay api.", ex);
        }
    }

    public void setUpayApiUrl(String upayApiUrl) {
        this.upayApiUrl = upayApiUrl;
    }
}
