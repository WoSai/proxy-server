package com.wosai.upay.proxy.core.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wosai.data.util.CollectionUtil;
import com.wosai.upay.proxy.core.exception.ProxyCoreBizException;
import com.wosai.upay.proxy.core.exception.ProxyCoreClientException;
import com.wosai.upay.proxy.core.exception.ProxyCoreSystemException;
import com.wosai.upay.proxy.core.model.Store;
import com.wosai.upay.proxy.core.model.Terminal;
import com.wosai.upay.proxy.core.service.ProxyCoreService;

@Controller
public class ProxyCoreController {
    private static final Logger logger = LoggerFactory.getLogger(ProxyCoreController.class); 

    @Autowired
    private ProxyCoreService proxyService;

    @RequestMapping(value="/store/create", method=RequestMethod.POST, produces="application/json")
    @ResponseBody
    public Map<String, Object> createStore(@RequestBody Map<String, Object> params) {
    	return success(proxyService.createStore(params));
    }

    @RequestMapping(value="/store/update", method=RequestMethod.POST, produces="application/json")
    @ResponseBody
    public Map<String, Object> updateStore(@RequestBody Map<String, Object> params) {
    	return success(proxyService.updateStore(params));
    }

    @RequestMapping(value="/store/get", method=RequestMethod.POST, produces="application/json")
    @ResponseBody
    public Map<String, Object> getStore(@RequestBody Map<String, Object> params) {
    	return success(proxyService.getStore((String)params.get(Store.ID)));
    }

    @RequestMapping(value="/terminal/create", method=RequestMethod.POST, produces="application/json")
    @ResponseBody
    public Map<String, Object> createTerminal(@RequestBody Map<String, Object> params) {
    	return success(proxyService.createTerminal(params));
    }

    @RequestMapping(value="/terminal/update", method=RequestMethod.POST, produces="application/json")
    @ResponseBody
    public Map<String, Object> updateTerminal(@RequestBody Map<String, Object> params) {
    	return success(proxyService.updateTerminal(params));
        
    }

    @RequestMapping(value="/terminal/move", method=RequestMethod.POST, produces="application/json")
    @ResponseBody
    public Map<String, Object> moveTerminal(@RequestBody Map<String, Object> params) {
    	return success(proxyService.moveTerminal(params));
        
    }

    @RequestMapping(value="/terminal/activate", method=RequestMethod.POST, produces="application/json")
    @ResponseBody
    public Map<String, Object> activateTerminal(@RequestBody Map<String, Object> params) {
    	return success(proxyService.activateTerminal(params));
    }

    @RequestMapping(value="/terminal/get", method=RequestMethod.POST, produces="application/json")
    @ResponseBody
    public Map<String, Object> getTerminal(@RequestBody Map<String, Object> params) {
    	return success(proxyService.getTerminal((String)params.get(Terminal.ID)));
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
    @ExceptionHandler(ProxyCoreClientException.class)
    @ResponseBody
    public Map<String, Object> handleValidationException(ProxyCoreClientException ex) {
        return CollectionUtil.hashMap("result", "400",
                                      "error_code", ex.getCode(),
                                      "error_message", ex.getMessage());

    }

    @SuppressWarnings("unchecked")
    @ExceptionHandler(ProxyCoreBizException.class)
    @ResponseBody
    public Map<String, Object> handleBizException(ProxyCoreBizException ex) {
        return CollectionUtil.hashMap("result", "500",
                                      "error_code",  ex.getCode(),
                                      "error_message", ex.getMessage());
    }
    
    @SuppressWarnings("unchecked")
    @ExceptionHandler(ProxyCoreSystemException.class)
    @ResponseBody
    public Map<String, Object> handleSystemException(ProxyCoreSystemException ex) {
        logger.error("System exception.", ex);
        return CollectionUtil.hashMap("result", "-1",
                                      "error_code",  ex.getCode(),
                                      "error_message", ex.getMessage());
    }
    
    @SuppressWarnings("unchecked")
    @ExceptionHandler(Throwable.class)
    @ResponseBody
    public Map<String, Object> handleUnknownError(Throwable ex) {
        logger.error("Unknown system error.", ex);
        return CollectionUtil.hashMap("result", "-1",
                                      "error_code",  "UNKNOWN_SYSTEM_ERROR",
                                      "error_message", ex.getMessage());
    }

}
