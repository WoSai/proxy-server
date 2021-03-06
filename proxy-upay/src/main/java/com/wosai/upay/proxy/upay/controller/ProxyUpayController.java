package com.wosai.upay.proxy.upay.controller;

import java.util.Map;

import org.hibernate.validator.method.MethodConstraintViolation;
import org.hibernate.validator.method.MethodConstraintViolationException;
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
import com.wosai.upay.proxy.upay.exception.ProxyUpayBizException;
import com.wosai.upay.proxy.upay.exception.ProxyUpayClientException;
import com.wosai.upay.proxy.upay.exception.ProxyUpaySystemException;
import com.wosai.upay.proxy.upay.model.TerminalKey;
import com.wosai.upay.proxy.upay.service.ProxyUpayService;

@Controller
public class ProxyUpayController {
    private static final Logger logger = LoggerFactory.getLogger(ProxyUpayController.class); 

    @Autowired
    private ProxyUpayService proxyService;

    @RequestMapping(value="/pay", method=RequestMethod.POST, produces="application/json")
    @ResponseBody
    public Map<String, Object> pay(@RequestBody Map<String, Object> params) {
        return success(proxyService.pay(params));
    }

    @RequestMapping(value="/preCreate", method=RequestMethod.POST, produces="application/json")
    @ResponseBody
    public Map<String, Object> preceate(@RequestBody Map<String, Object> params) {
        return success(proxyService.precreate(params));
    }

    @RequestMapping(value="/query", method=RequestMethod.POST, produces="application/json")
    @ResponseBody
    public Map<String, Object> query(@RequestBody Map<String, Object> params) {
        return success(proxyService.query(params));
    }

    @RequestMapping(value="/refund", method=RequestMethod.POST, produces="application/json")
    @ResponseBody
    public Map<String, Object> refund(@RequestBody Map<String, Object> params) {
        return success(proxyService.refund(params));
    }

    @RequestMapping(value="/revoke", method=RequestMethod.POST, produces="application/json")
    @ResponseBody
    public Map<String, Object> revoke(@RequestBody Map<String, Object> params) {
        return success(proxyService.revoke(params));
    }

    @RequestMapping(value="/cancel", method=RequestMethod.POST, produces="application/json")
    @ResponseBody
    public Map<String, Object> cancel(@RequestBody Map<String, Object> params) {
        return success(proxyService.cancel(params));
    }

    @RequestMapping(value="/init", method=RequestMethod.POST, produces="application/json")
    @ResponseBody
    public Map<String, Object> init(@RequestBody Map<String, Object> params) {
    	String terminalSn=params.get(TerminalKey.TERMINAL_SN).toString(); 
    	String secret=params.get(TerminalKey.TERMINAL_KEY).toString(); 
    	proxyService.init(terminalSn,secret);
    	return success();
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> success(Object serviceResult) {
        return CollectionUtil.hashMap("result_code", "200",
                                      "biz_response", serviceResult);
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> success() {
    	return CollectionUtil.hashMap("result_code", "200",
                "biz_response", CollectionUtil.hashMap(
              		  "result_code","SUCCESS"
              		  ));
    }

    @SuppressWarnings("unchecked")
    @ExceptionHandler(ProxyUpayClientException.class)
    @ResponseBody
    public Map<String, Object> handleClientException(ProxyUpayClientException ex) {
        return CollectionUtil.hashMap("result", "400",
                                      "error_code", ex.getCode(),
                                      "error_message", ex.getMessage());

    }

    @SuppressWarnings("unchecked")
    @ExceptionHandler(ProxyUpayBizException.class)
    @ResponseBody
    public Map<String, Object> handleBizException(ProxyUpayBizException ex) {
        return CollectionUtil.hashMap("result", "600",
                                      "error_code",  ex.getCode(),
                                      "error_message", ex.getMessage());
    }
    
    @SuppressWarnings("unchecked")
    @ExceptionHandler(ProxyUpaySystemException.class)
    @ResponseBody
    public Map<String, Object> handleSystemException(ProxyUpaySystemException ex) {
        return CollectionUtil.hashMap("result", "500",
                                      "error_code",  ex.getCode(),
                                      "error_message", ex.getMessage());
    }
    
    @SuppressWarnings("unchecked")
    @ExceptionHandler(Throwable.class)
    @ResponseBody
    public Map<String, Object> handleUnknownError(Throwable ex) {
        return CollectionUtil.hashMap("result", "-1",
                                      "error_code",  "UNKNOWN_SYSTEM_ERROR",
                                      "error_message", ex.getMessage());
    }

}
