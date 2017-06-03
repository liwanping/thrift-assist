package com.github.thrift.wrapper.spring.thrift;

/**
 * Created by frank.li on 2017/3/27.
 */
public class ThriftServiceProxyFactoryBean extends AbstractThriftServiceProxyFactoryBean {

    @Override
    protected void initProxy() {
        this.setProxyInterface(this.loadThriftServiceClass());
        this.setTargetClass(this.getServiceInterface());
    }
}
