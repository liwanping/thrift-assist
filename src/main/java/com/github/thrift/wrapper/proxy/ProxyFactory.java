package com.github.thrift.wrapper.proxy;

/**
 * Created by frank.li on 2017/4/17.
 */
public interface ProxyFactory {

    Object getProxy(ProxyInvokable proxyInvokable);
}
