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
    public static Map<String,Object> resolve(Map result) throws ResponseResolveException{
    	if(result!=null
    			&&result.get(Response.RESULT_CODE)!=null
    			&&result.get(Response.RESULT_CODE).equals(Response.RESULT_CODE_SUCEESS)){
    		Map response=(Map) result.get(Response.BIZ_RESPONSE);
    		if(response!=null
    				&&response.get(Response.RESULT_CODE)!=null
    				&&response.get(Response.RESULT_CODE).equals(Response.RESPONSE_CODE_SUCEESS)){
    			return (Map<String, Object>) response.get(Response.DATA);
    		}
    	} 
    	throw new ResponseResolveException("Unable to resolve business data.");
    }
	
}
