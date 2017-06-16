package com.github.thrift.assist.proxy;

/**
 * Created by frank.li on 2017/5/27.
 */
public class ProxyInvoker implements ProxyInvokable {

    protected Class<?> proxyInterface;
    protected Class<?> targetClass;
    protected Object target;

    protected ProxyFactory proxyFactory;

    @Override
    public Class<?> getProxyInterface() {
        return proxyInterface;
    }

    public void setProxyInterface(Class<?> proxyInterface) {
        this.proxyInterface = proxyInterface;
    }

    @Override
    public Class<?> getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(Class<?> targetClass) {
        this.targetClass = targetClass;
    }

    @Override
    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
        if (target != null) {
            this.targetClass = target.getClass();
        }
    }

    @Override
    public ProxyFactory getProxyFactory() {
        return proxyFactory;
    }

    public void setProxyFactory(ProxyFactory proxyFactory) {
        this.proxyFactory = proxyFactory;
    }

    @Override
    public String[] getWrappedExceptions() {
        return null;
    }

    @Override
    public String getTargetMethod(String interfaceMethod) {
        return interfaceMethod;
    }
}
