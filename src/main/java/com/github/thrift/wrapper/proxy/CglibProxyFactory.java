package com.github.thrift.wrapper.proxy;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * Created by frank.li on 2017/4/17.
 */
public class CglibProxyFactory implements ProxyFactory {

    @Override
    public Object getProxy(final ProxyInvokable proxyInvokable) {

        Enhancer enhancer = new Enhancer();
        enhancer.setInterfaces(new Class[]{proxyInvokable.getProxyInterface()});
        enhancer.setCallback(new MethodInterceptor() {

            @Override
            public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
                return MethodProxyHandler.getInstance().invoke(proxyInvokable, method, args);
            }
        });

        return enhancer.create();
    }
}
