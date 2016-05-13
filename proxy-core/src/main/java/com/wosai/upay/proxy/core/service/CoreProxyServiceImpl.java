package com.wosai.upay.proxy.core.service;

import java.util.Map;

import org.hibernate.validator.internal.engine.MethodConstraintViolationImpl;
import org.hibernate.validator.method.MethodConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.wosai.upay.proxy.core.exception.ParameterValidationException;
import com.wosai.upay.proxy.core.exception.ProxyCoreException;

@Service @Validated
public class CoreProxyServiceImpl implements ProxyCoreService {

    @Autowired
    private VendorApiFacade vendorApi;

    @Override
    public Map<String, Object> createStore(Map<String, Object> request)

                                      throws ProxyCoreException {
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

    		return vendorApi.getStore(storeId);
    	}catch(MethodConstraintViolationException mcve){
			throw parseParameterValidationException(mcve);
    	}
    }

    @Override
    public Map<String, Object> createTerminal(Map<String, Object> request)
            throws ProxyCoreException {
    	try{
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

    		return vendorApi.getTerminal(terminalId);
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
		return new ParameterValidationException(((MethodConstraintViolationImpl)mcve.getConstraintViolations().iterator().next()).getMessage());
	}

}
