package com.wosai.upay.proxy.core.service;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.validator.internal.engine.MethodConstraintViolationImpl;
import org.hibernate.validator.method.MethodConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.wosai.upay.proxy.core.exception.ParameterValidationException;
import com.wosai.upay.proxy.core.exception.ProxyCoreException;
import com.wosai.upay.proxy.core.model.Store;
import com.wosai.upay.proxy.core.model.Terminal;

@Service @Validated
public class CoreProxyServiceImpl implements ProxyCoreService {

    @Autowired
    private VendorApiFacade vendorApi;

    @Override
    public Map<String, Object> createStore(Map<String, Object> request)

                                      throws ProxyCoreException {
    	if(request.get(Store.NAME)==null){
    		request.put(Store.NAME, request.get(Store.CLIENT_SN));
    	}
    	try{
    		return vendorApi.createStore(request);
    	}catch(MethodConstraintViolationException mcve){
			throw parseParameterValidationException(mcve);
    	}
    }

    @Override
    public Map<String, Object> updateStore(Map<String, Object> request)
            throws ProxyCoreException {
    	try{
    	
    		return vendorApi.updateStore(request);
    	}catch(MethodConstraintViolationException mcve){
			throw parseParameterValidationException(mcve);
    	}
    }

    @Override
    public Map<String, Object> getStore(String storeId) throws ProxyCoreException {
    	try{

    		Map<String,Object> map=new HashMap<String,Object>();
    		map.put(Store.ID, storeId);
    		return vendorApi.getStore(map);
    	}catch(MethodConstraintViolationException mcve){
			throw parseParameterValidationException(mcve);
    	}
    }

    @Override
    public Map<String, Object> createTerminal(Map<String, Object> request)
            throws ProxyCoreException {
    	try{
        	if(request.get(Terminal.NAME)==null){
        		request.put(Terminal.NAME, request.get(Terminal.CLIENT_SN));
        	}
    		return vendorApi.createTerminal(request);
    	}catch(MethodConstraintViolationException mcve){
			throw parseParameterValidationException(mcve);
    	}
    }

    @Override
    public Map<String, Object> updateTerminal(Map<String, Object> request)
            throws ProxyCoreException {
    	try{

	    	return vendorApi.updateTerminal(request);
		}catch(MethodConstraintViolationException mcve){
			throw parseParameterValidationException(mcve);
		}
        
    }
    
    @Override
    public Map<String, Object> activateTerminal(Map<String, Object> request)
            throws ProxyCoreException {
    	try{

    		return vendorApi.activateTerminal(request);
    	}catch(MethodConstraintViolationException mcve){
			throw parseParameterValidationException(mcve);
    	}
    }

    @Override
    public Map<String, Object> moveTerminal(Map<String, Object> request) throws ProxyCoreException {
    	try{

    		return vendorApi.moveTerminal(request);
    	}catch(MethodConstraintViolationException mcve){
			throw parseParameterValidationException(mcve);
    	}
    }

    @Override
    public Map<String, Object> getTerminal(String terminalId) throws ProxyCoreException {
    	try{


    		Map<String,Object> request=new HashMap<String,Object>();
    		request.put(Terminal.ID, terminalId);
    		return vendorApi.getTerminal(request);
    	}catch(MethodConstraintViolationException mcve){
			throw parseParameterValidationException(mcve);
    	}
    }

	public void setVendorApi(VendorApiFacade vendorApi) {
		this.vendorApi = vendorApi;
	}
	
	/**
	 * 转换验证异常
	 * @param mcve
	 * @return
	 */
	public ParameterValidationException parseParameterValidationException(MethodConstraintViolationException mcve){
		MethodConstraintViolationImpl mcvi=(MethodConstraintViolationImpl)mcve.getConstraintViolations().iterator().next();
		Map<Object,Object> invalidValue=(Map<Object,Object>)mcvi.getConstraintDescriptor().getAttributes();
		String key = String.valueOf(invalidValue.get("value"));
		return new ParameterValidationException(new StringBuilder("invalid ").append(key).append(".").toString());
	}

}
