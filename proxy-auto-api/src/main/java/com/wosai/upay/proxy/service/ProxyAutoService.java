package com.wosai.upay.proxy.service;

import java.util.Map;

import com.googlecode.jsonrpc4j.JsonRpcService;
import com.wosai.upay.proxy.exception.ProxyAutoException;

@JsonRpcService("rpc/core")
public interface ProxyAutoService {
    Map<String, Object> pay(Map<String, Object> request) throws ProxyAutoException;
    Map<String, Object> precreate(Map<String, Object> request) throws ProxyAutoException;
    Map<String, Object> query(Map<String, Object> request) throws ProxyAutoException;
    Map<String, Object> refund(Map<String, Object> request) throws ProxyAutoException;
    Map<String, Object> revoke(Map<String, Object> request) throws ProxyAutoException;

    String createStore(Map<String, Object> request) throws ProxyAutoException;
    void updateStore(Map<String, Object> request)  throws ProxyAutoException;
    Map<String, String> getStore(String sn) throws ProxyAutoException;

    String createTerminal(Map<String, Object> request) throws ProxyAutoException;
    void updateTerminal(Map<String, Object> request) throws ProxyAutoException;
    Map<String, String> getTerminal(String sn) throws ProxyAutoException;
}
