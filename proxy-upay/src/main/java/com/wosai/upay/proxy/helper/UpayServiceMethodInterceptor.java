package com.wosai.upay.proxy.helper;


import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.hibernate.validator.method.MethodConstraintViolation;
import org.hibernate.validator.method.MethodConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wosai.upay.proxy.upay.exception.ParameterValidationException;
import com.wosai.upay.proxy.upay.exception.ProxyUpayBizException;
import com.wosai.upay.proxy.upay.exception.ProxyUpaySystemException;
import com.wosai.upay.util.MapUtil;

public class UpayServiceMethodInterceptor implements MethodInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(UpayServiceMethodInterceptor.class);
    
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        
        long before = 0;
        if (logger.isTraceEnabled()) {
            before = System.currentTimeMillis();
            logger.trace("invoking {}", new MethodInvocationFormatter(invocation));
        }
        try {
            Object result = invocation.proceed();
            MapUtil.removeNullValues(result);
            return result;

        } catch(MethodConstraintViolationException ex) {
            StringBuilder sb = new StringBuilder();
            for(MethodConstraintViolation<?> violation: ex.getConstraintViolations()) {
                if (sb.length() > 0)
                    sb.append("\n");
                sb.append(violation.getMessage());
            }
            throw new ParameterValidationException(sb.toString());

        } catch (ProxyUpayBizException ex) {
            logger.warn(String.format("biz error in proxy-upay service %s", ex.getCode()), ex);
            throw ex;

        } catch (ProxyUpaySystemException ex) {
            logger.error(String.format("sys error in proxy-upay service %s", ex.getCode()), ex);
            throw ex;
            
        } catch (Throwable ex) {
            logger.error("unknown critical error in proxy-upay service", ex);
            throw ex;
            
        } finally {
            if (logger.isTraceEnabled()) {
                long duration = System.currentTimeMillis() - before;
                logger.trace("{} ms in {}", duration, new MethodInvocationFormatter(invocation));
            }
        }
    }

    class MethodInvocationFormatter {
        private final MethodInvocation invocation;
        public MethodInvocationFormatter(MethodInvocation invocation) {
            this.invocation = invocation;
        }
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("method ").append(invocation.getMethod()).append(" with arguments ");
            for(Object arg: invocation.getArguments()) {
                sb.append("\n").append(arg);
            }
            return sb.toString();
        }
    }
}
