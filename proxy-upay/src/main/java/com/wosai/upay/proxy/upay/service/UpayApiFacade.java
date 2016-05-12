package com.wosai.upay.proxy.upay.service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;

import com.wosai.upay.httpclient.UpayHttpClient;
import com.wosai.upay.proxy.exception.RemoteResponse400;
import com.wosai.upay.proxy.exception.RemoteResponse500;
import com.wosai.upay.proxy.upay.model.Order;
import com.wosai.upay.proxy.upay.model.TerminalKey;
import com.wosai.upay.proxy.util.ResponseUtil;
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
    private String uploadLogApiUrl;
    
    private long failedWaitTime=30000;

    @Autowired
    private RawUpayHttpClient client;

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
                                   Map<String, Object> request) throws IOException, RemoteResponse400, RemoteResponse500 {

        // 直接返回支付网关的结果
    	String url=new StringBuilder(upayApiDomain).append(payApiUrl).toString();
    	
        return call(terminalSn, terminalKey, url, request);
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
                                         Map<String, Object> request) throws IOException, RemoteResponse400, RemoteResponse500 {

        // 直接返回支付网关的结果
    	String url=new StringBuilder(upayApiDomain).append(refundApiUrl).toString();
    	
        return call(terminalSn, terminalKey, url, request);
    }
    
    public Map<String, Object> query(@NotEmpty(message="终端号不能为空")
                                         String terminalSn,
                                         @NotEmpty(message="终端密钥不能为空")
                                         String terminalKey,
                                         Map<String, Object> request) throws IOException, RemoteResponse400, RemoteResponse500 {

        String url=new StringBuilder(upayApiDomain).append(queryApiUrl).toString();
        return call(terminalSn, terminalKey, url, request);
    }
    
    public Map<String, Object> cancel(@NotEmpty(message="终端号不能为空")
                                         String terminalSn,
                                         @NotEmpty(message="终端密钥不能为空")
                                         String terminalKey,
                                         Map<String, Object> request) throws IOException, RemoteResponse400, RemoteResponse500 {

        String url=new StringBuilder(upayApiDomain).append(cancelApiUrl).toString();
        return call(terminalSn, terminalKey, url, request);
    }
    
    public Map<String, Object> revoke(@NotEmpty(message="终端号不能为空")
                                         String terminalSn,
                                         @NotEmpty(message="终端密钥不能为空")
                                         String terminalKey,
                                         Map<String, Object> request) throws IOException, RemoteResponse400, RemoteResponse500 {

        String url=new StringBuilder(upayApiDomain).append(revokeApiUrl).toString();
        return call(terminalSn, terminalKey, url, request);
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
                                         Map<String, Object> request) throws IOException, RemoteResponse400, RemoteResponse500 {

        // 直接返回支付网关的结果
    	String url=new StringBuilder(upayApiDomain).append(precreateApiUrl).toString();
        return call(terminalSn, terminalKey, url, request);
    }
    
    public Map<String, Object> checkin(@NotEmpty(message="终端号不能为空")
									   String terminalSn,
									   @NotEmpty(message="终端密钥不能为空")
									   String terminalKey,
                                       String deviceId) throws IOException, RemoteResponse400, RemoteResponse500 {

        Map<String,Object> request=new HashMap<String, Object>();
    	request.put(TerminalKey.TERMINAL_SN, terminalSn);
    	request.put(TerminalKey.DEVICE_ID, deviceId);
        // 直接返回支付网关的结果
    	String url=new StringBuilder(upayApiDomain).append(checkinApiUrl).toString();
    	return call(terminalSn, terminalKey, url, request);
    }
    
    public Map<String, Object> uploadLog(@NotEmpty(message="终端号不能为空")
									   String terminalSn,
									   @NotEmpty(message="终端密钥不能为空")
									   String terminalKey,
                                       @NotEmpty(message="日志内容不能为空")
                                       String content) throws IOException {
        // 直接返回支付网关的结果
    	String url=new StringBuilder(upayApiDomain).append(uploadLogApiUrl).toString();
    	return client.call(terminalSn, terminalKey, url, content);
    }

    private Map<String, Object> call(String terminalSn, String terminalKey, String url, Map<String,Object> request)
            throws RemoteResponse400, RemoteResponse500, IOException {

        return ResponseUtil.resolve1(client.call(terminalSn, terminalKey, url, request));
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

	public void setClient(RawUpayHttpClient client) {
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

	public void setUploadLogApiUrl(String uploadLogApiUrl) {
		this.uploadLogApiUrl = uploadLogApiUrl;
	}

}
