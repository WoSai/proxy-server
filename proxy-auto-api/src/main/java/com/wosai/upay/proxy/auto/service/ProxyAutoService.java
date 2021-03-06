package com.wosai.upay.proxy.auto.service;

import java.util.Map;

import com.googlecode.jsonrpc4j.JsonRpcService;
import com.wosai.upay.proxy.auto.exception.ProxyAutoException;
import com.wosai.upay.proxy.core.exception.ProxyCoreException;
import com.wosai.upay.proxy.upay.exception.ProxyUpayException;

@JsonRpcService("rpc/auto")
public interface ProxyAutoService {
    Map<String, Object> pay(Map<String, Object> request) throws ProxyAutoException, ProxyCoreException, ProxyUpayException;
    Map<String, Object> precreate(Map<String, Object> request) throws ProxyAutoException, ProxyCoreException, ProxyUpayException;
    Map<String, Object> query(Map<String, Object> request) throws ProxyAutoException, ProxyCoreException, ProxyUpayException;
    Map<String, Object> refund(Map<String, Object> request) throws ProxyAutoException, ProxyCoreException, ProxyUpayException;
    Map<String, Object> revoke(Map<String, Object> request) throws ProxyAutoException, ProxyCoreException, ProxyUpayException;
    Map<String, Object> cancel(Map<String, Object> request) throws ProxyAutoException, ProxyCoreException, ProxyUpayException;
    
    Map<String, Object> createStore(Map<String, Object> request) throws ProxyAutoException;
    Map<String, Object> updateStore(Map<String, Object> request)  throws ProxyAutoException;
    Map<String, Object> getStore(Map<String, Object> request) throws ProxyAutoException;

    Map<String, Object> createTerminal(Map<String, Object> request) throws ProxyAutoException;
    Map<String, Object> updateTerminal(Map<String, Object> request) throws ProxyAutoException;
    Map<String, Object> getTerminal(Map<String, Object> request) throws ProxyAutoException;
	Map<String, Object> activateTerminal(Map<String, Object> request) throws ProxyAutoException;
	void uploadLog() throws ProxyAutoException;
	Map<String, Object> moveTerminal(Map<String, Object> request) throws ProxyAutoException;

	
}
