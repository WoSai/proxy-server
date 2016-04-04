package com.wosai.upay.proxy.core.service;

import java.util.Map;

import com.googlecode.jsonrpc4j.JsonRpcService;
import com.wosai.upay.proxy.core.exception.ProxyCoreException;

@JsonRpcService("rpc/core")
public interface ProxyCoreService {

    Map<String, Object> createStore(Map<String, Object> request) throws ProxyCoreException;
    void updateStore(Map<String, Object> request)  throws ProxyCoreException;
    Map<String, Object> getStore(String sn) throws ProxyCoreException;

    Map<String, Object> createTerminal(Map<String, Object> request) throws ProxyCoreException;
    void updateTerminal(Map<String, Object> request) throws ProxyCoreException;
    Map<String, Object> getTerminal(String sn) throws ProxyCoreException;
}
