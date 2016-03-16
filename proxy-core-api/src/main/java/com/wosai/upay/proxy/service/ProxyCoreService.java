package com.wosai.upay.proxy.service;

import java.util.Map;

import com.googlecode.jsonrpc4j.JsonRpcService;
import com.wosai.upay.proxy.exception.ProxyCoreException;

@JsonRpcService("rpc/core")
public interface ProxyCoreService {

    String createStore(Map<String, Object> request) throws ProxyCoreException;
    void updateStore(Map<String, Object> request)  throws ProxyCoreException;
    Map<String, String> getStore(String sn) throws ProxyCoreException;

    String createTerminal(Map<String, Object> request) throws ProxyCoreException;
    void updateTerminal(Map<String, Object> request) throws ProxyCoreException;
    Map<String, String> getTerminal(String sn) throws ProxyCoreException;
}
