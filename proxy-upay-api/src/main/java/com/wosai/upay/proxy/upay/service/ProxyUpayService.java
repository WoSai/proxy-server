package com.wosai.upay.proxy.upay.service;

import java.util.Map;

import com.googlecode.jsonrpc4j.JsonRpcService;
import com.wosai.upay.proxy.upay.exception.ProxyUpayException;

@JsonRpcService("rpc/upay")
public interface ProxyUpayService {

    void init(String terminalSn, String secret) throws ProxyUpayException;
    Map<String, Object> pay(Map<String, Object> request) throws ProxyUpayException;
    Map<String, Object> precreate(Map<String, Object> request) throws ProxyUpayException;
    Map<String, Object> query(Map<String, Object> request) throws ProxyUpayException;
    Map<String, Object> refund(Map<String, Object> request) throws ProxyUpayException;
    Map<String, Object> revoke(Map<String, Object> request) throws ProxyUpayException;
}
