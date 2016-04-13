package com.wosai.upay.proxy.util;

import java.util.Map;

import com.wosai.upay.proxy.exception.BizResponseResolveException;
import com.wosai.upay.proxy.exception.ResponseResolveException;
import com.wosai.upay.proxy.model.Response;

public class ResponseUtil {
    @SuppressWarnings("unchecked")
    public static Map<String,Object> resolve1(Map<String,Object> result) throws ResponseResolveException {
        if (result == null) {
            throw new ResponseResolveException("NULL_RESPONSE", "null response");
        }
        if (Response.RESULT_CODE_SUCEESS.equals(result.get(Response.RESULT_CODE))) {
            return (Map<String,Object>)result.get(Response.BIZ_RESPONSE);

        } else {
            throw new ResponseResolveException((String)result.get(Response.ERROR_CODE),
                                               (String)result.get(Response.ERROR_MESSAGE));
        }
        
    }
    
    @SuppressWarnings("unchecked")
	public static Map<String,Object> resolve2(Map<String,Object> result) throws ResponseResolveException, BizResponseResolveException {
        Map<String, Object> bizResponse = (Map<String, Object>)resolve1(result);
        if (bizResponse == null) {
            throw new BizResponseResolveException("NULL_BIZ_RESPONSE", "null biz response");
        }
        String bizResultCode = (String)bizResponse.get(Response.RESULT_CODE);
        if (bizResultCode == null) {
            throw new ResponseResolveException("NULL_BIZ_RESULT_CODE", "null biz result code");
        }
        if (!bizResultCode.contains("FAIL")) {
            return (Map<String,Object>)bizResponse.get(Response.DATA);
        }else{
            throw new ResponseResolveException((String)bizResponse.get(Response.ERROR_CODE),
                                               (String)bizResponse.get(Response.ERROR_MESSAGE));
        }
        
    }
	
}
