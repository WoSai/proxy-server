package com.wosai.upay.proxy.upay.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;

import com.wosai.upay.httpclient.UpayHttpClient;
import com.wosai.upay.proxy.upay.model.Order;
import com.wosai.upay.proxy.upay.model.TerminalKey;
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
    private String checkinApiUrl;
    
    private long failedWaitTime=30000;

    @Autowired
    private UpayHttpClient client;

    public Map<String, Object> pay(@NotEmpty(message="终端号不能为空")
                                   String terminalSn,
                                   @NotEmpty(message="终端密钥不能为空")
                                   String terminalKey,
                                   @PropNotEmpty.List({
                                       @PropNotEmpty(value=Order.CLIENT_SN,message="商户系统订单号不能为空"),
                                       @PropNotEmpty(value=Order.TOTAL_AMOUNT,message="交易总金额不能为空"),
                                       @PropNotEmpty(value=Order.DYNAMIC_ID,message="条码内容不能为空"),
                                       @PropNotEmpty(value=Order.SUBJECT,message="交易简介不能为空"),
                                       @PropNotEmpty(value=Order.OPERATOR,message="门店操作员不能为空")
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
                                             @PropNotEmpty(value=Order.CLIENT_SN,message="商户系统订单号不能为空"),
                                             @PropNotEmpty(value=Order.REFUND_AMOUNT,message="退款金额不能为空"),
                                             @PropNotEmpty(value=Order.REFUND_REQUEST_NO,message="退款序列号不能为空"),
                                             @PropNotEmpty(value=Order.OPERATOR,message="退款操作员不能为空")
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
                                         Map<String, Object> request) throws IOException{

        String url=new StringBuilder(upayApiDomain).append(queryApiUrl).toString();
        return client.call(terminalSn, terminalKey, url, request);
    }
    
    public Map<String, Object> cancel(@NotEmpty(message="终端号不能为空")
                                         String terminalSn,
                                         @NotEmpty(message="终端密钥不能为空")
                                         String terminalKey,
                                         Map<String, Object> request) throws IOException {

        String url=new StringBuilder(upayApiDomain).append(cancelApiUrl).toString();
        return client.call(terminalSn, terminalKey, url, request);
    }
    
    public Map<String, Object> revoke(@NotEmpty(message="终端号不能为空")
                                         String terminalSn,
                                         @NotEmpty(message="终端密钥不能为空")
                                         String terminalKey,
                                         Map<String, Object> request) throws IOException {

        String url=new StringBuilder(upayApiDomain).append(revokeApiUrl).toString();
        return client.call(terminalSn, terminalKey, url, request);
    }
    
    public Map<String, Object> precreate(@NotEmpty(message="终端号不能为空")
                                         String terminalSn,
                                         @NotEmpty(message="终端密钥不能为空")
                                         String terminalKey,
                                         @PropNotEmpty.List({
                                             @PropNotEmpty(value=Order.CLIENT_SN,message="商户系统订单号不能为空"),
                                             @PropNotEmpty(value=Order.TOTAL_AMOUNT,message="交易总金额不能为空"),
                                             @PropNotEmpty(value=Order.PAYWAY,message="支付方式不能为空"),
                                             @PropNotEmpty(value=Order.SUBJECT,message="交易简介不能为空"),
                                             @PropNotEmpty(value=Order.OPERATOR,message="门店操作员不能为空")
                                         })
                                         Map<String, Object> request) throws IOException {

        // 直接返回支付网关的结果
    	String url=new StringBuilder(upayApiDomain).append(precreateApiUrl).toString();
        return client.call(terminalSn, terminalKey, url, request);
    }
    
    public Map<String, Object> checkin(@NotEmpty(message="终端号不能为空")
									   String terminalSn,
									   @NotEmpty(message="终端密钥不能为空")
									   String terminalKey,
                                       @NotEmpty(message="设备编号不能为空")
                                       String deviceId) throws IOException {

        Map<String,Object> request=new HashMap<String, Object>();
    	request.put(TerminalKey.TERMINAL_SN, terminalSn);
    	request.put(TerminalKey.DEVICE_ID, deviceId);
        // 直接返回支付网关的结果
    	String url=new StringBuilder(upayApiDomain).append(checkinApiUrl).toString();
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

	public void setCheckinApiUrl(String checkinApiUrl) {
		this.checkinApiUrl = checkinApiUrl;
	}

}
