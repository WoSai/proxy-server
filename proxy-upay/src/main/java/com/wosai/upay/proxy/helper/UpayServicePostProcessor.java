package com.wosai.upay.proxy.helper;

import java.lang.annotation.Annotation;

import org.aopalliance.aop.Advice;
import org.springframework.aop.Advisor;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.framework.AopInfrastructureBean;
import org.springframework.aop.framework.ProxyConfig;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import com.wosai.upay.helper.UpayServiceAnnotation;

@SuppressWarnings("serial")
public class UpayServicePostProcessor extends ProxyConfig
        implements BeanPostProcessor, BeanClassLoaderAware, Ordered, InitializingBean {

    private Class<? extends Annotation> annotationType = UpayServiceAnnotation.class;

    private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();

    private Advisor advisor;


    public void setValidatedAnnotationType(Class<? extends Annotation> annotationType) {
        Assert.notNull(annotationType, "'annotationType' must not be null");
        this.annotationType = annotationType;
    }

    public void setBeanClassLoader(ClassLoader classLoader) {
        this.beanClassLoader = classLoader;
    }

    public int getOrder() {
        // This should run after all other post-processors, so that it can just add
        // an advisor to existing proxies rather than double-proxy.
        return LOWEST_PRECEDENCE;
    }


    public void afterPropertiesSet() {
        Pointcut pointcut = new AnnotationMatchingPointcut(this.annotationType, true);
        Advice advice = new UpayServiceMethodInterceptor();
        this.advisor = new DefaultPointcutAdvisor(pointcut, advice);
    }


    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof AopInfrastructureBean) {
            // Ignore AOP infrastructure such as scoped proxies.
            return bean;
        }
        Class<?> targetClass = AopUtils.getTargetClass(bean);
        if (AopUtils.canApply(this.advisor, targetClass)) {
            /*
            if (bean instanceof Advised) {
                ((Advised) bean).addAdvisor(this.advisor);
                return bean;
            }
            else {
                ProxyFactory proxyFactory = new ProxyFactory(bean);
                // Copy our properties (proxyTargetClass etc) inherited from ProxyConfig.
                proxyFactory.copyFrom(this);
                proxyFactory.addAdvisor(this.advisor);
                return proxyFactory.getProxy(this.beanClassLoader);
            }
            */
            ProxyFactory proxyFactory = new ProxyFactory(bean);
            // Copy our properties (proxyTargetClass etc) inherited from ProxyConfig.
            proxyFactory.copyFrom(this);
            proxyFactory.addAdvisor(this.advisor);
            return proxyFactory.getProxy(this.beanClassLoader);
        }
        else {
            // This is not a repository.
            return bean;
        }
    }

}
