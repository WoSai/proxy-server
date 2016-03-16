package com.wosai.upay.proxy.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wosai.upay.proxy.exception.ProxyCoreException;

@Service
public class CoreProxyServiceImpl implements ProxyCoreService {

    @Autowired
    private VendorApiFacade vendorApi;

    @Override
    public String createStore(Map<String, Object> request)

                                      throws ProxyCoreException {
        return vendorApi.createStore(request);
    }

    @Override
    public void updateStore(Map<String, Object> request)
            throws ProxyCoreException {

        // TODO Auto-generated method stub
        
    }

    @Override
    public Map<String, String> getStore(String sn) throws ProxyCoreException {

        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String createTerminal(Map<String, Object> request)
            throws ProxyCoreException {

        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void updateTerminal(Map<String, Object> request)
            throws ProxyCoreException {

        // TODO Auto-generated method stub
        
    }

    @Override
    public Map<String, String> getTerminal(String sn) throws ProxyCoreException {

        // TODO Auto-generated method stub
        return null;
    }

}
