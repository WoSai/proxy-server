package com.wosai.upay.proxy.auto.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wosai.data.util.CollectionUtil;
import com.wosai.upay.proxy.auto.exception.ProxyAutoBizException;
import com.wosai.upay.proxy.auto.exception.ProxyAutoClientException;
import com.wosai.upay.proxy.auto.exception.ProxyAutoSystemException;
import com.wosai.upay.proxy.auto.service.LogService;
import com.wosai.upay.proxy.auto.service.ProxyAutoService;
import com.wosai.upay.proxy.core.exception.ProxyCoreBizException;
import com.wosai.upay.proxy.core.exception.ProxyCoreClientException;
import com.wosai.upay.proxy.core.exception.ProxyCoreSystemException;
import com.wosai.upay.proxy.upay.exception.ProxyUpayBizException;
import com.wosai.upay.proxy.upay.exception.ProxyUpayClientException;
import com.wosai.upay.proxy.upay.exception.ProxyUpaySystemException;

@Controller @Deprecated
@RequestMapping("/auto")
public class ProxyAutoController {
    private static final Logger logger = LoggerFactory.getLogger(ProxyAutoController.class); 

    @Autowired
    private ProxyAutoService proxyService; 
    @Autowired
    private LogService logService; 

    // 门店管理
    @RequestMapping(value="/store/create", method=RequestMethod.POST, produces="application/json")
    @ResponseBody
    public Map<String, Object> createStore(@RequestBody Map<String, Object> params) {
        return success(proxyService.createStore(params));
    }

    @RequestMapping(value="/store/update", method=RequestMethod.POST, produces="application/json")
    @ResponseBody @Deprecated
    public Map<String, Object> updateStore(@RequestBody Map<String, Object> params,HttpServletRequest request) {
        return success(proxyService.updateStore(params));
    }
    
    //终端管理
    @RequestMapping(value="/terminal/create", method=RequestMethod.POST, produces="application/json")
    @ResponseBody
    public Map<String, Object> createTerminal(@RequestBody Map<String, Object> params) {
        return success(proxyService.createTerminal(params));
    }

    @RequestMapping(value="/terminal/update", method=RequestMethod.POST, produces="application/json")
    @ResponseBody @Deprecated
    public Map<String, Object> updateTerminal(Map<String, Object> params,HttpServletRequest request) {
        return success(proxyService.updateTerminal(params));
    }
    
    @RequestMapping(value="/terminal/activate", method=RequestMethod.POST, produces="application/json")
    @ResponseBody @Deprecated
    public Map<String, Object> activateTerminal(@RequestBody Map<String, Object> params,HttpServletRequest request) {
        long start = System.nanoTime();
        String level = "info";
        try{
        	Map<String, Object> result = proxyService.activateTerminal(params);
            return result;
        }catch(Exception e){
        	level = "error";
        	throw e;
        }finally{
            long end = System.nanoTime();
            logService.logRequest(request, level, end-start);
        }
       
    }

    // 交易
    @RequestMapping(value="/pay", method=RequestMethod.POST, produces="application/json")
    @ResponseBody @Deprecated
    public Map<String, Object> pay(@RequestBody Map<String, Object> params,HttpServletRequest request) {
        long start = System.nanoTime();
        String level = "info";
        try{
	    	Map<String, Object> result = proxyService.pay(params);
	        return result;
        }catch(Exception e){
        	level = "error";
        	throw e;
        }finally{
            long end = System.nanoTime();
            logService.logRequest(request, level, end-start);
        }
    }

    @RequestMapping(value="/preCreate", method=RequestMethod.POST, produces="application/json")
    @ResponseBody @Deprecated
    public Map<String, Object> preceate(@RequestBody Map<String, Object> params,HttpServletRequest request) {
        long start = System.nanoTime();
        String level = "info";
        try{
	    	Map<String, Object> result = proxyService.precreate(params);
	        return result;
        }catch(Exception e){
        	level = "error";
        	throw e;
        }finally{
            long end = System.nanoTime();
            logService.logRequest(request, level, end-start);
        }
    }

    @RequestMapping(value="/query", method=RequestMethod.POST, produces="application/json")
    @ResponseBody @Deprecated
    public Map<String, Object> query(@RequestBody Map<String, Object> params,HttpServletRequest request) {
        long start = System.nanoTime();
        String level = "info";
        try{
	    	Map<String, Object> result = proxyService.query(params);
	        return result;
        }catch(Exception e){
        	level = "error";
        	throw e;
        }finally{
            long end = System.nanoTime();
            logService.logRequest(request, level, end-start);
        }
    }

    @RequestMapping(value="/refund", method=RequestMethod.POST, produces="application/json")
    @ResponseBody @Deprecated
    public Map<String, Object> refund(@RequestBody Map<String, Object> params,HttpServletRequest request) {
        long start = System.nanoTime();
        String level = "info";
        try{
	    	Map<String, Object> result = proxyService.refund(params);
	        return result;
        }catch(Exception e){
        	level = "error";
        	throw e;
        }finally{
            long end = System.nanoTime();
            logService.logRequest(request, level, end-start);
        }
    }

    @RequestMapping(value="/revoke", method=RequestMethod.POST, produces="application/json")
    @ResponseBody @Deprecated
    public Map<String, Object> revoke(@RequestBody Map<String, Object> params,HttpServletRequest request) {
        long start = System.nanoTime();
        String level = "info";
        try{
	    	Map<String, Object> result = proxyService.revoke(params);
	        return result;
        }catch(Exception e){
        	level = "error";
        	throw e;
        }finally{
            long end = System.nanoTime();
            logService.logRequest(request, level, end-start);
        }
    }

    @RequestMapping(value="/cancel", method=RequestMethod.POST, produces="application/json")
    @ResponseBody @Deprecated
    public Map<String, Object> cancel(@RequestBody Map<String, Object> params,HttpServletRequest request) {
        long start = System.nanoTime();
        String level = "info";
        try{
	    	Map<String, Object> result = proxyService.cancel(params);
	        return result;
        }catch(Exception e){
        	level = "error";
        	throw e;
        }finally{
            long end = System.nanoTime();
            logService.logRequest(request, level, end-start);
        }
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> success(Object serviceResult) {
        return CollectionUtil.hashMap("result_code", "200",
                                      "biz_response", CollectionUtil.hashMap(
                                    		  "result_code","SUCCESS",
                                    		  "data",serviceResult
                                    		  ));
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> success() {
    	return CollectionUtil.hashMap("result_code", "200",
                "biz_response", CollectionUtil.hashMap(
              		  "result_code","SUCCESS"
              		  ));
    }

    @SuppressWarnings("unchecked")
    @ExceptionHandler(ProxyAutoClientException.class)
    @ResponseBody
    public Map<String, Object> handleClientException(ProxyAutoClientException ex) {
        return CollectionUtil.hashMap("result_code", "400",
                                      "error_code", ex.getCode(),
                                      "error_message", ex.getMessage());

    }

    @SuppressWarnings("unchecked")
    @ExceptionHandler(ProxyUpayClientException.class)
    @ResponseBody
    public Map<String, Object> handleUpayClientException(ProxyUpayClientException ex) {
        return CollectionUtil.hashMap("result_code", "401",
                                      "error_code", ex.getCode(),
                                      "error_message", ex.getMessage());

    }

    @SuppressWarnings("unchecked")
    @ExceptionHandler(ProxyCoreClientException.class)
    @ResponseBody
    public Map<String, Object> handleCoreClientException(ProxyCoreClientException ex) {
        return CollectionUtil.hashMap("result_code", "402",
                                      "error_code", ex.getCode(),
                                      "error_message", ex.getMessage());

    }

    @SuppressWarnings("unchecked")
    @ExceptionHandler(ProxyAutoBizException.class)
    @ResponseBody
    public Map<String, Object> handleBizException(ProxyAutoBizException ex) {
        return CollectionUtil.hashMap("result_code", "600",
                                      "error_code",  ex.getCode(),
                                      "error_message", ex.getMessage());
    }
    
    @SuppressWarnings("unchecked")
    @ExceptionHandler(ProxyUpayBizException.class)
    @ResponseBody
    public Map<String, Object> handleUpayBizException(ProxyUpayBizException ex) {
        return CollectionUtil.hashMap("result_code", "601",
                                      "error_code",  ex.getCode(),
                                      "error_message", ex.getMessage());
    }

    @SuppressWarnings("unchecked")
    @ExceptionHandler(ProxyCoreBizException.class)
    @ResponseBody
    public Map<String, Object> handleCoreBizException(ProxyCoreBizException ex) {
        return CollectionUtil.hashMap("result_code", "602",
                                      "error_code",  ex.getCode(),
                                      "error_message", ex.getMessage());
    }

    @SuppressWarnings("unchecked")
    @ExceptionHandler(ProxyAutoSystemException.class)
    @ResponseBody
    public Map<String, Object> handleSystemException(ProxyAutoSystemException ex) {
        return CollectionUtil.hashMap("result_code", "500",
                                      "error_code",  ex.getCode(),
                                      "error_message", ex.getMessage());
    }
    
    @SuppressWarnings("unchecked")
    @ExceptionHandler(ProxyUpaySystemException.class)
    @ResponseBody
    public Map<String, Object> handleUpaySystemException(ProxyUpaySystemException ex) {
        return CollectionUtil.hashMap("result_code", "501",
                                      "error_code",  ex.getCode(),
                                      "error_message", ex.getMessage());
    }

    @SuppressWarnings("unchecked")
    @ExceptionHandler(ProxyCoreSystemException.class)
    @ResponseBody
    public Map<String, Object> handleCoreSystemException(ProxyCoreSystemException ex) {
        return CollectionUtil.hashMap("result_code", "502",
                                      "error_code",  ex.getCode(),
                                      "error_message", ex.getMessage());
    }

    @SuppressWarnings("unchecked")
    @ExceptionHandler(Throwable.class)
    @ResponseBody
    public Map<String, Object> handleUnknownError(Throwable ex) {
        return CollectionUtil.hashMap("result_code", "-1",
                                      "error_code",  "UNKNOWN_SYSTEM_ERROR",
                                      "error_message", ex.getMessage());
    }

	public void setProxyService(ProxyAutoService proxyService) {
		this.proxyService = proxyService;
	}

	public void setLogService(LogService logService) {
		this.logService = logService;
	}
    
}
