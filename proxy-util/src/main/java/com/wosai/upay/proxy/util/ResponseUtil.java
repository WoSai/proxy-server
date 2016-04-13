package com.wosai.upay.proxy.util;

import java.util.Map;

import com.wosai.upay.proxy.exception.ResponseResolveException;
import com.wosai.upay.proxy.model.Response;

public class ResponseUtil {

	/**
     * 解析请求响应中的业务数据
     * @param result
     * @return
     * @throws VendorApiException
     */
    @SuppressWarnings("unchecked")
	public static Map<String,Object> resolveCore(Map<String,Object> result) throws ResponseResolveException{
    	if(result!=null
    			&&result.get(Response.RESULT_CODE)!=null
    			&&result.get(Response.RESULT_CODE).equals(Response.RESULT_CODE_SUCEESS)){
			Map<String,Object> response=(Map<String,Object>) result.get(Response.BIZ_RESPONSE);
    		return response;
    	} 
    	throw new ResponseResolveException("Unable to resolve business data.");
    }

	/**
     * 解析请求响应中的业务数据
     * @param result
     * @return
     * @throws VendorApiException
     */
    @SuppressWarnings("unchecked")
	public static Map<String,Object> resolveUpay(Map<String,Object> result) throws ResponseResolveException{
    	if(result!=null
    			&&result.get(Response.RESULT_CODE)!=null
    			&&result.get(Response.RESULT_CODE).equals(Response.RESULT_CODE_SUCEESS)){
			Map<String,Object> response=(Map<String,Object>) result.get(Response.BIZ_RESPONSE);
			if(response!=null
				&&response.get(Response.RESULT_CODE)!=null
		    	&&(response.get(Response.RESULT_CODE).equals(Response.RESPONSE_CODE_SUCEESS)
		    	 ||response.get(Response.RESULT_CODE).equals(Response.RESPONSE_CODE_PRECREATE_SUCCESS))){
				Map<String,Object> bizData=(Map<String,Object>) response.get(Response.DATA);
	    		return bizData;
			}
    	} 
    	throw new ResponseResolveException("Unable to resolve business data.");
    }
	
}
