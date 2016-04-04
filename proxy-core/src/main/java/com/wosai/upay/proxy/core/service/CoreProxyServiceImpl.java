package com.wosai.upay.proxy.core.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.wosai.upay.proxy.core.exception.ProxyCoreException;
import com.wosai.upay.proxy.core.exception.ProxyCoreResolveException;
import com.wosai.upay.proxy.exception.ResponseResolveException;
import com.wosai.upay.proxy.model.Response;
import com.wosai.upay.proxy.util.ResponseUtil;

@Service @Validated
public class CoreProxyServiceImpl implements ProxyCoreService {

    @Autowired
    private VendorApiFacade vendorApi;

    @Override
    public Map<String, Object> createStore(Map<String, Object> request)

                                      throws ProxyCoreException {
    	Map<String, Object> response=vendorApi.createStore(request);
    	try {
			return ResponseUtil.resolve(response);
		} catch (ResponseResolveException e) {
			String resultCode=(String) response.get(Response.RESULT_CODE);
			String errorCode=(String) response.get(Response.ERROR_CODE);
			String errorMessage=(String) response.get(Response.RESULT_CODE);
			throw new ProxyCoreResolveException(resultCode,errorCode,errorMessage);
		}
    }

    @Override
    public void updateStore(Map<String, Object> request)
            throws ProxyCoreException {
    	
    	vendorApi.updateStore(request);
    }

    @Override
    public Map<String, Object> getStore(String sn) throws ProxyCoreException {

    	Map<String, Object> response=vendorApi.getStore(sn);
    	try {
	    	return ResponseUtil.resolve(response);
		} catch (ResponseResolveException e) {
			//转发服务端错误信息
			throw this.parseException(response);
		}
    }

    @Override
    public Map<String, Object> createTerminal(Map<String, Object> request)
            throws ProxyCoreException {
    	Map<String, Object> response=vendorApi.createTerminal(request);
    	try {
	    	return ResponseUtil.resolve(response);
		} catch (ResponseResolveException e) {
			//转发服务端错误信息
			throw this.parseException(response);
		}
    }

    @Override
    public void updateTerminal(Map<String, Object> request)
            throws ProxyCoreException {

    	vendorApi.updateTerminal(request);
        
    }

    @Override
    public Map<String, Object> getTerminal(String sn) throws ProxyCoreException {

    	Map<String, Object> response=vendorApi.getTerminal(sn);
    	try {
	    	return ResponseUtil.resolve(response);
		} catch (ResponseResolveException e) {
			//转发服务端错误信息
			throw this.parseException(response);
		}
    }

	public void setVendorApi(VendorApiFacade vendorApi) {
		this.vendorApi = vendorApi;
	}
	
	public ProxyCoreResolveException parseException(Map<String, Object> response){
		String resultCode=(String) response.get(Response.RESULT_CODE);
		String errorCode=(String) response.get(Response.ERROR_CODE);
		String errorMessage=(String) response.get(Response.RESULT_CODE);
		return new ProxyCoreResolveException(resultCode,errorCode,errorMessage);
	}
    
}
