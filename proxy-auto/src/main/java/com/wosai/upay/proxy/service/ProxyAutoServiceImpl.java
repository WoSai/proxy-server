package com.wosai.upay.proxy.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wosai.upay.proxy.exception.ProxyAutoException;

@Service
public class ProxyAutoServiceImpl implements ProxyAutoService {

    @Autowired
    private ProxyUpayService proxyUpay;
    @Autowired
    private ProxyCoreService proxyCore;

    @Override
    public Map<String, Object> pay(Map<String, Object> request)
            throws ProxyAutoException {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public Map<String, Object> precreate(Map<String, Object> request)
            throws ProxyAutoException {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public Map<String, Object> query(Map<String, Object> request)
            throws ProxyAutoException {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public Map<String, Object> refund(Map<String, Object> request)
            throws ProxyAutoException {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public Map<String, Object> revoke(Map<String, Object> request)
            throws ProxyAutoException {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public String createStore(Map<String, Object> request)
            throws ProxyAutoException {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public void updateStore(Map<String, Object> request)
            throws ProxyAutoException {
        // TODO Auto-generated method stub
        
    }
    @Override
    public Map<String, String> getStore(String sn) throws ProxyAutoException {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public String createTerminal(Map<String, Object> request)
            throws ProxyAutoException {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public void updateTerminal(Map<String, Object> request)
            throws ProxyAutoException {
        // TODO Auto-generated method stub
        
    }
    @Override
    public Map<String, String> getTerminal(String sn) throws ProxyAutoException {
        // TODO Auto-generated method stub
        return null;
    }


}
