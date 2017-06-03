package com.github.thrift.wrapper.spring.thrift;

import com.github.thrift.wrapper.annotation.ThriftMapping;
import com.github.thrift.wrapper.spring.ProxyFactoryBean;

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
        if (!this.serviceInterface.isAnnotationPresent(ThriftMapping.class)) {
            throw new RuntimeException("Failed to create thrift service client: " + serviceInterface.getName() +
                    ", must specify thrift class with annotation: @ThriftMapping");
        }

        initProxy();
    }

    public boolean isAsynchronous() {
        return asynchronous;
    }

    public void setAsynchronous(boolean asynchronous) {
        this.asynchronous = asynchronous;
    }

    protected Class<?> loadThriftServiceClass() {

        Class<?> thriftServiceClass = this.serviceInterface.getAnnotation(ThriftMapping.class).value();
        String thriftServiceIface = thriftServiceClass.getName() +
                (this.asynchronous ? THRIFT_ASYNCIFACE : THRIFT_IFACE);

        try {
            return Class.forName(thriftServiceIface);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Failed to load thrift service class: " + thriftServiceIface, e);
        }
    }
}
