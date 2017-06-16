package com.github.thrift.assist.spring.thrift;

import com.github.thrift.assist.annotation.Mapping;
import com.github.thrift.assist.spring.ProxyFactoryBean;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;

/**
 * Created by frank.li on 2017/3/27.
 */
public abstract class AbstractThriftServiceProxyFactoryBean extends ProxyFactoryBean {

    private static final String THRIFT_IFACE = "$Iface";
    private static final String THRIFT_ASYNCIFACE = "$AsyncIface";

    private Class<?> serviceInterface;
    private boolean asynchronous = false;

    /**
     * Override to set the different proxyInterface and targetClass
     */
    protected abstract void initProxy();

    public Class<?> getServiceInterface() {
        return serviceInterface;
    }

    public void setServiceInterface(Class<?> serviceInterface) {
        this.serviceInterface = serviceInterface;
        if (!this.serviceInterface.isAnnotationPresent(Mapping.class)) {
            throw new RuntimeException("Failed to create thrift proxy for service: " + serviceInterface.getName() +
                    ", must specify thrift class with annotation: @Mapping");
        }

        initProxy();
    }

    @Override
    public String getTargetMethod(String interfaceMethod) {

        for (Method method : serviceInterface.getMethods()) {
            if (method.isAnnotationPresent(Mapping.class)) {
                Mapping mapping = method.getAnnotation(Mapping.class);
                if (StringUtils.isNotEmpty(mapping.method())) {
                    if (interfaceMethod.equals(method.getName())) {
                        return mapping.method();
                    }
                    if (interfaceMethod.equals(mapping.method())) {
                        return method.getName();
                    }
                }
            }
        }

        return super.getTargetMethod(interfaceMethod);
    }

    public boolean isAsynchronous() {
        return asynchronous;
    }

    public void setAsynchronous(boolean asynchronous) {
        this.asynchronous = asynchronous;
    }

    protected Class<?> loadThriftServiceClass() {

        Class<?> thriftServiceClass = this.serviceInterface.getAnnotation(Mapping.class).value();
        String thriftServiceIface = thriftServiceClass.getName() +
                (this.asynchronous ? THRIFT_ASYNCIFACE : THRIFT_IFACE);

        try {
            return Class.forName(thriftServiceIface);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Failed to load thrift service class: " + thriftServiceIface, e);
        }
    }
}
