package com.github.thrift.assist.spring.thrift;

/**
 * Created by frank.li on 2017/3/27.
 */
public class ThriftServiceClientProxyFactoryBean extends AbstractThriftServiceProxyFactoryBean {

    @Override
    protected void initProxy() {
        this.setProxyInterface(this.getServiceInterface());
        this.setTargetClass(this.loadThriftServiceClass());
    }

    @Override
    public String[] getWrappedExceptions() {
        return new String[]{"org.apache.thrift.TException"};
    }
}
