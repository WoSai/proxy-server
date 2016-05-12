package com.wosai.upay.proxy.core.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.wosai.upay.proxy.core.exception.ProxyCoreException;
import com.wosai.upay.proxy.core.model.Terminal;

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
    public Map<String, Object> updateStore(Map<String, Object> request)
            throws ProxyCoreException {
    	
    	return vendorApi.updateStore(request);
    }

    @Override
    public Map<String, Object> getStore(String storeId) throws ProxyCoreException {

    	return vendorApi.getStore(storeId);
    }

    @Override
    public Map<String, Object> createTerminal(Map<String, Object> request)
            throws ProxyCoreException {
    	return vendorApi.createTerminal(request);
    }

    @Override
    public Map<String, Object> updateTerminal(Map<String, Object> request)
            throws ProxyCoreException {

    	return vendorApi.updateTerminal(request);
        
    }
    
    @Override
    public Map<String, Object> activateTerminal(Map<String, Object> request)
            throws ProxyCoreException {

        return vendorApi.activateTerminal(request);
    }

    @Override
    public Map<String, Object> moveTerminal(Map<String, Object> request) throws ProxyCoreException {

    	return vendorApi.moveTerminal(request);
    }

    @Override
    public Map<String, Object> getTerminal(String terminalId) throws ProxyCoreException {

    	return vendorApi.getTerminal(terminalId);
    }

	public void setVendorApi(VendorApiFacade vendorApi) {
		this.vendorApi = vendorApi;
	}

}
