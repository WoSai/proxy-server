package com.wosai.upay.proxy.auto.controller;

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
import com.wosai.upay.proxy.auto.exception.ProxyAutoBizException;
import com.wosai.upay.proxy.auto.exception.ProxyAutoClientException;
import com.wosai.upay.proxy.auto.exception.ProxyAutoSystemException;
import com.wosai.upay.proxy.auto.service.ProxyAutoService;

@Controller
@RequestMapping("/auto")
public class ProxyAutoController {
    private static final Logger logger = LoggerFactory.getLogger(ProxyAutoController.class); 

    @Autowired
    private ProxyAutoService proxyService; 

    // 门店管理
    @RequestMapping(value="/store/create", method=RequestMethod.POST, produces="application/json")
    @ResponseBody
    public Map<String, Object> createStore(@RequestBody Map<String, Object> params) {
        return proxyService.createStore(params);
    }

    @RequestMapping(value="/store/update", method=RequestMethod.POST, produces="application/json")
    @ResponseBody
    public Map<String, Object> updateStore(@RequestBody Map<String, Object> params) {
        proxyService.updateStore(params);
        return success();
    }
    
    //终端管理
    @RequestMapping(value="/terminal/create", method=RequestMethod.POST, produces="application/json")
    @ResponseBody
    public Map<String, Object> createTerminal(@RequestBody Map<String, Object> params) {
        return proxyService.createTerminal(params);
    }

    @RequestMapping(value="/terminal/update", method=RequestMethod.POST, produces="application/json")
    @ResponseBody
    public Map<String, Object> updateTerminal(Map<String, Object> params) {
        proxyService.updateTerminal(params);
        return success();
    }
    
    @RequestMapping(value="/terminal/activate", method=RequestMethod.POST, produces="application/json")
    @ResponseBody
    public Map<String, Object> activateTerminal(@RequestBody Map<String, Object> params) {
        return proxyService.activateTerminal(params);
    }

    // 交易
    @RequestMapping(value="/pay", method=RequestMethod.POST, produces="application/json")
    @ResponseBody
    public Map<String, Object> pay(@RequestBody Map<String, Object> params) {
        return proxyService.pay(params);
    }

    @RequestMapping(value="/preCreate", method=RequestMethod.POST, produces="application/json")
    @ResponseBody
    public Map<String, Object> preceate(@RequestBody Map<String, Object> params) {
        return proxyService.precreate(params);
    }

    @RequestMapping(value="/query", method=RequestMethod.POST, produces="application/json")
    @ResponseBody
    public Map<String, Object> query(@RequestBody Map<String, Object> params) {
        return proxyService.query(params);
    }

    @RequestMapping(value="/refund", method=RequestMethod.POST, produces="application/json")
    @ResponseBody
    public Map<String, Object> refund(@RequestBody Map<String, Object> params) {
        return proxyService.refund(params);
    }

    @RequestMapping(value="/revoke", method=RequestMethod.POST, produces="application/json")
    @ResponseBody
    public Map<String, Object> revoke(@RequestBody Map<String, Object> params) {
        return proxyService.revoke(params);
    }

    @RequestMapping(value="/cancel", method=RequestMethod.POST, produces="application/json")
    @ResponseBody
    public Map<String, Object> cancel(@RequestBody Map<String, Object> params) {
        return proxyService.cancel(params);
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> success(Object serviceResult) {
        return CollectionUtil.hashMap("result_code", "200",
                                      "data", serviceResult);
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> success() {
        return CollectionUtil.hashMap("result_code", "200");
    }

    @SuppressWarnings("unchecked")
    @ExceptionHandler(ProxyAutoClientException.class)
    @ResponseBody
    public Map<String, Object> handleValidationException(ProxyAutoClientException ex) {
        return CollectionUtil.hashMap("result_code", "400",
                                      "error_code", ex.getCode(),
                                      "error_message", ex.getMessage());

    }

    @SuppressWarnings("unchecked")
    @ExceptionHandler(ProxyAutoBizException.class)
    @ResponseBody
    public Map<String, Object> handleBizException(ProxyAutoBizException ex) {
        return CollectionUtil.hashMap("result_code", "500",
                                      "error_code",  ex.getCode(),
                                      "error_message", ex.getMessage());
    }
    
    @SuppressWarnings("unchecked")
    @ExceptionHandler(ProxyAutoSystemException.class)
    @ResponseBody
    public Map<String, Object> handleSystemException(ProxyAutoSystemException ex) {
        logger.error("System exception.", ex);
        return CollectionUtil.hashMap("result_code", "-1",
                                      "error_code",  ex.getCode(),
                                      "error_message", ex.getMessage());
    }
    
    @SuppressWarnings("unchecked")
    @ExceptionHandler(Throwable.class)
    @ResponseBody
    public Map<String, Object> handleUnknownError(Throwable ex) {
        logger.error("Unknown system error.", ex);
        return CollectionUtil.hashMap("result_code", "-1",
                                      "error_code",  "UNKNOWN_SYSTEM_ERROR",
                                      "error_message", ex.getMessage());
    }
}
