package com.wosai.upay.proxy.util;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wosai.upay.proxy.exception.RemoteResponse400;
import com.wosai.upay.proxy.exception.RemoteResponse500;
import com.wosai.upay.proxy.exception.RemoteResponseBizError;
import com.wosai.upay.proxy.model.Response;

public class ResponseUtil {
    private static final Logger logger = LoggerFactory.getLogger(ResponseUtil.class); 
    @SuppressWarnings("unchecked")
    public static Map<String,Object> resolve1(Map<String,Object> result) throws RemoteResponse400, RemoteResponse500 {
    	logger.debug(new StringBuilder("call api result:").append(""+result).toString());
    	
    	if (Response.RESULT_CODE_CLIENT_ERROR.equals(result.get(Response.RESULT_CODE))) {
            throw new RemoteResponse400(String.valueOf(result.get(Response.ERROR_CODE)),
                                        (String.valueOf(result.get(Response.ERROR_MESSAGE))));
        } else if (Response.RESULT_CODE_SYSTEM_ERROR.equals(result.get(Response.RESULT_CODE))){
            throw new RemoteResponse500(String.valueOf(result.get(Response.ERROR_CODE)),
            		String.valueOf(result.get(Response.ERROR_MESSAGE)));
        } else {
            return (Map<String,Object>)result.get(Response.BIZ_RESPONSE);
        }
        
    }
    
    @SuppressWarnings("unchecked")
	public static Map<String,Object> resolve2(Map<String,Object> result) throws RemoteResponse400, RemoteResponse500, RemoteResponseBizError {
    	logger.debug(new StringBuilder("call api result:").append(""+result).toString());

    	Map<String, Object> bizResponse = (Map<String, Object>)resolve1(result);

        if (bizResponse == null) {
            return null;
        }
        String bizResultCode = (String)bizResponse.get(Response.RESULT_CODE);
        if (bizResultCode == null) {
            return null;
        }

        if (!bizResultCode.contains("FAIL")) {
        	Map<String,Object> data = (Map<String,Object>)bizResponse.get(Response.DATA);
        	if(data!=null){
        		return data;
        	}else{
                throw new RemoteResponse500("",
                		Response.BIZ_RESPONSE_DATA_ERROR);
        	}
        }else{
            throw new RemoteResponseBizError(String.valueOf(bizResponse.get(Response.ERROR_CODE)),
                                             String.valueOf(bizResponse.get(Response.ERROR_MESSAGE)));
        }
        
    }
	
}
