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

    private String upayApiDomain;

    private String payApiUrl;
    private String refundApiUrl;
    private String queryApiUrl;
    private String cancelApiUrl;
    private String revokeApiUrl;
    private String precreateApiUrl;
    
    private long failedWaitTime=30000;

    @Autowired
    private UpayHttpClient client;

    public Map<String, Object> pay(@NotEmpty(message="终端号不能为空")
                                   String terminalSn,
                                   @NotEmpty(message="终端密钥不能为空")
                                   String terminalKey,
                                   @PropNotEmpty.List({
                                       @PropNotEmpty(Order.CLIENT_SN),
                                   })
                                   Map<String, Object> request) throws IOException {

        // 直接返回支付网关的结果
    	String url=new StringBuilder(upayApiDomain).append(payApiUrl).toString();
        return client.call(terminalSn, terminalKey, url, request);
    }
    
    public Map<String, Object> refund(@NotEmpty(message="终端号不能为空")
                                         String terminalSn,
                                         @NotEmpty(message="终端密钥不能为空")
                                         String terminalKey,
                                         @PropNotEmpty.List({
                                             @PropNotEmpty(Order.CLIENT_SN),
                                         })
                                         Map<String, Object> request) throws IOException {

        // 直接返回支付网关的结果
    	String url=new StringBuilder(upayApiDomain).append(refundApiUrl).toString();
        return client.call(terminalSn, terminalKey, url, request);
    }
    
    public Map<String, Object> query(@NotEmpty(message="终端号不能为空")
                                         String terminalSn,
                                         @NotEmpty(message="终端密钥不能为空")
                                         String terminalKey,
                                         @PropNotEmpty.List({
                                             @PropNotEmpty(Order.CLIENT_SN),
                                         })
                                         Map<String, Object> request) throws UpayApiException {

    	try {
            // 直接返回支付网关的结果
        	String url=new StringBuilder(upayApiDomain).append(queryApiUrl).toString();
            return client.call(terminalSn, terminalKey, url, request);
        }catch(IOException ex) {
            throw new UpayApiException("Failed to call upay api.", ex);
        }
    }
    
    public Map<String, Object> cancel(@NotEmpty(message="终端号不能为空")
                                         String terminalSn,
                                         @NotEmpty(message="终端密钥不能为空")
                                         String terminalKey,
                                         @PropNotEmpty.List({
                                             @PropNotEmpty(Order.CLIENT_SN),
                                         })
                                         Map<String, Object> request) throws UpayApiException {

    	try {
            // 直接返回支付网关的结果
        	String url=new StringBuilder(upayApiDomain).append(cancelApiUrl).toString();
            return client.call(terminalSn, terminalKey, url, request);
        }catch(IOException ex) {
            throw new UpayApiException("Failed to call upay api.", ex);
        }
    }
    
    public Map<String, Object> revoke(@NotEmpty(message="终端号不能为空")
                                         String terminalSn,
                                         @NotEmpty(message="终端密钥不能为空")
                                         String terminalKey,
                                         @PropNotEmpty.List({
                                             @PropNotEmpty(Order.CLIENT_SN),
                                         })
                                         Map<String, Object> request) throws UpayApiException {

    	try {
            // 直接返回支付网关的结果
        	String url=new StringBuilder(upayApiDomain).append(revokeApiUrl).toString();
            return client.call(terminalSn, terminalKey, url, request);
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
                                         Map<String, Object> request) throws IOException {

        // 直接返回支付网关的结果
    	String url=new StringBuilder(upayApiDomain).append(precreateApiUrl).toString();
        return client.call(terminalSn, terminalKey, url, request);
    }

	public void setUpayApiDomain(String upayApiDomain) {
		this.upayApiDomain = upayApiDomain;
	}

	public void setPayApiUrl(String payApiUrl) {
		this.payApiUrl = payApiUrl;
	}

	public void setRefundApiUrl(String refundApiUrl) {
		this.refundApiUrl = refundApiUrl;
	}

	public void setQueryApiUrl(String queryApiUrl) {
		this.queryApiUrl = queryApiUrl;
	}

	public void setCancelApiUrl(String cancelApiUrl) {
		this.cancelApiUrl = cancelApiUrl;
	}

	public void setRevokeApiUrl(String revokeApiUrl) {
		this.revokeApiUrl = revokeApiUrl;
	}

	public void setPrecreateApiUrl(String precreateApiUrl) {
		this.precreateApiUrl = precreateApiUrl;
	}

	public void setClient(UpayHttpClient client) {
		this.client = client;
	}

	public void setFailedWaitTime(long failedWaitTime) {
		this.failedWaitTime = failedWaitTime;
	}

	public long getFailedWaitTime() {
		return failedWaitTime;
	}

}
