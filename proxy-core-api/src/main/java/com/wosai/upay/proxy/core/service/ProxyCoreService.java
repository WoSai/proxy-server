package com.wosai.upay.proxy.core.service;

import java.util.Map;

import com.googlecode.jsonrpc4j.JsonRpcService;
import com.wosai.upay.proxy.core.exception.ProxyCoreException;

@JsonRpcService("rpc/core")
public interface ProxyCoreService {

    Map<String, Object> createStore(Map<String, Object> request) throws ProxyCoreException;
    Map<String, Object> updateStore(Map<String, Object> request)  throws ProxyCoreException;
    Map<String, Object> getStore(String storeId) throws ProxyCoreException;

    Map<String, Object> createTerminal(Map<String, Object> request) throws ProxyCoreException;
    Map<String, Object> updateTerminal(Map<String, Object> request) throws ProxyCoreException;
    Map<String, Object> getTerminal(String terminalId) throws ProxyCoreException;
    Map<String, Object> activateTerminal(Map<String, Object> request) throws ProxyCoreException;
}
