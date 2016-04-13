package com.wosai.upay.proxy.auto.service;

import static java.lang.String.format;
import static org.springframework.util.ClassUtils.convertClassNameToResourcePath;
import static org.springframework.util.ResourceUtils.CLASSPATH_URL_PREFIX;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.googlecode.jsonrpc4j.ExceptionResolver;
import com.googlecode.jsonrpc4j.JsonRpcHttpClient;
import com.googlecode.jsonrpc4j.JsonRpcService;
import com.googlecode.jsonrpc4j.spring.AutoJsonRpcClientProxyCreator;

/**
 * 修改默认异常处理
 * @author qi
 *
 */
public class ProxyAutoJsonRpcClientProxyCreator extends AutoJsonRpcClientProxyCreator {
	private static final Logger LOG = Logger.getLogger(AutoJsonRpcClientProxyCreator.class.getName());

    private ApplicationContext applicationContext;

    private String scanPackage;

    private URL baseUrl;

    private ObjectMapper objectMapper;
    
    private ExceptionResolver exceptionResolver;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        SimpleMetadataReaderFactory metadataReaderFactory = new SimpleMetadataReaderFactory(applicationContext);
        DefaultListableBeanFactory dlbf = (DefaultListableBeanFactory) beanFactory;
        String resolvedPath = resolvePackageToScan();
        LOG.fine(format("Scanning '%s' for JSON-RPC service interfaces.", resolvedPath));
        try {
            for (Resource resource : applicationContext.getResources(resolvedPath)) {
                if (resource.isReadable()) {
                    MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
                    ClassMetadata classMetadata = metadataReader.getClassMetadata();
                    AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();
                    String jsonRpcPathAnnotation = JsonRpcService.class.getName();
                    if (annotationMetadata.isAnnotated(jsonRpcPathAnnotation)) {
                        String className = classMetadata.getClassName();
                        String path = (String) annotationMetadata.getAnnotationAttributes(jsonRpcPathAnnotation).get("value");
                        boolean useNamedParams = (Boolean) annotationMetadata.getAnnotationAttributes(jsonRpcPathAnnotation).get("useNamedParams");
                        LOG.fine(format("Found JSON-RPC service to proxy [%s] on path '%s'.", className, path));
                        registerJsonProxyBean(dlbf, className, path, useNamedParams);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(format("Cannot scan package '%s' for classes.", resolvedPath), e);
        }
    }

    /**
     * Converts the scanPackage to something that the resource loader can handle.
     */
    private String resolvePackageToScan() {
        return CLASSPATH_URL_PREFIX + convertClassNameToResourcePath(scanPackage) + "/**/*.class";
    }

    /**
     * Registers a new proxy bean with the bean factory.
     */
    private void registerJsonProxyBean(DefaultListableBeanFactory dlbf, String className, String path, boolean useNamedParams) {
    	JsonRpcHttpClient jsonRpcHttpClient;
    	BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder
                .rootBeanDefinition(ProxyJsonProxyFactoryBean.class)
                .addPropertyValue("exceptionResolver", exceptionResolver)
                .addPropertyValue("serviceUrl", appendBasePath(path))
                .addPropertyValue("serviceInterface", className)
                .addPropertyValue("useNamedParams", useNamedParams);
        if (objectMapper != null) {
            beanDefinitionBuilder.addPropertyValue("objectMapper", objectMapper);
        }
        dlbf.registerBeanDefinition(className + "-clientProxy", beanDefinitionBuilder.getBeanDefinition());
    }

    /**
     * Appends the base path to the path found in the interface.
     */
    private String appendBasePath(String path) {
        try {
            return new URL(baseUrl, path).toString();
        } catch (MalformedURLException e) {
            throw new RuntimeException(format("Cannot combine URLs '%s' and '%s' to valid URL.", baseUrl, path), e);
        }
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void setBaseUrl(URL baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void setScanPackage(String scanPackage) {
        this.scanPackage = scanPackage;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

	public void setExceptionResolver(ExceptionResolver exceptionResolver) {
		this.exceptionResolver = exceptionResolver;
	}
    
}
