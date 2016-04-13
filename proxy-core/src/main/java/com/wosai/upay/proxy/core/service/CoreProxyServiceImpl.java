package com.wosai.upay.proxy.core.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.wosai.upay.proxy.core.exception.ProxyCoreException;

@Service @Validated
public class CoreProxyServiceImpl implements ProxyCoreService {

    @Autowired
    private VendorApiFacade vendorApi;

    @Override
    public Map<String, Object> createStore(Map<String, Object> request)

                                      throws ProxyCoreException {
    	return vendorApi.createStore(request);
    }

    @Override
    public void updateStore(Map<String, Object> request)
            throws ProxyCoreException {
    	
    	vendorApi.updateStore(request);
    }

    @Override
    public Map<String, Object> getStore(String sn) throws ProxyCoreException {

    	return vendorApi.getStore(sn);
    }

    @Override
    public Map<String, Object> createTerminal(Map<String, Object> request)
            throws ProxyCoreException {
    	return vendorApi.createTerminal(request);
    }

    @Override
    public void updateTerminal(Map<String, Object> request)
            throws ProxyCoreException {

    	vendorApi.updateTerminal(request);
        
    }
    
    @Override
    public Map<String, Object> activateTerminal(Map<String, Object> request)
            throws ProxyCoreException {

        return vendorApi.activateTerminal(request);
    }

    @Override
    public Map<String, Object> getTerminal(String sn) throws ProxyCoreException {

    	return vendorApi.getTerminal(sn);
    }

	public void setVendorApi(VendorApiFacade vendorApi) {
		this.vendorApi = vendorApi;
	}

}
