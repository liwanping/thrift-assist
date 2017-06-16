package com.github.thrift.assist.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by frank.li on 2017/4/17.
 */
public class JdkProxyFactory implements ProxyFactory {

    @Override
    public Object getProxy(final ProxyInvokable proxyInvokable) {

        return Proxy.newProxyInstance(this.getClass().getClassLoader(),
                new Class[]{proxyInvokable.getProxyInterface()},
                new InvocationHandler() {

                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        return MethodProxyHandler.getInstance().invoke(proxyInvokable, method, args);
                    }
                });
    }
}
