package com.github.thrift.assist.spring;

import com.github.thrift.assist.proxy.ProxyFactory;
import com.github.thrift.assist.proxy.ProxyInvoker;
import com.github.thrift.assist.proxy.ProxyType;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;

/**
 * Created by frank.li on 2017/3/25.
 */
public class ProxyFactoryBean extends ProxyInvoker implements FactoryBean<Object>, InitializingBean, ApplicationContextAware {

    private static final ProxyType DEFAULT_PROXY_TYPE = ProxyType.JAVASSIST;

    private static final String PROXY_TYPE_FACTORY_MAP_BEAN = "proxyTypeFactoryMap";

    private Object singletonInstance;

    private ProxyType proxyType;

    private ApplicationContext applicationContext;

    public ProxyFactoryBean() {
    }

    @Override
    public Object getObject() throws Exception {
        return getSingletonInstance();
    }

    private Object getSingletonInstance() {

        if (this.singletonInstance == null) {
            synchronized (ProxyFactoryBean.class) {
                if (this.singletonInstance == null) {
                    this.singletonInstance = this.getProxyFactory().getProxy(this);
                }
            }
        }

        return this.singletonInstance;
    }

    @Override
    public Class<?> getObjectType() {
        return this.proxyInterface;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public void setProxyType(ProxyType proxyType) {
        this.proxyType = proxyType;
    }


    @SuppressWarnings("unchecked")
    @Override
    public void afterPropertiesSet() throws Exception {

        if (this.target == null) {
            if (this.targetClass == null) {
                throw new RuntimeException("Missing target bean configuration!");
            }

            this.target = this.applicationContext.getBean(getTargetClass());
            if (this.target == null) {
                throw new RuntimeException("Cannot determine target bean with class: " + getTargetClass().getName());
            }
        }
        if (this.proxyType == null) {
            this.proxyType = DEFAULT_PROXY_TYPE;
        }
        this.proxyFactory = ((Map<ProxyType, ProxyFactory>) this.applicationContext
                .getBean(PROXY_TYPE_FACTORY_MAP_BEAN)).get(this.proxyType);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
