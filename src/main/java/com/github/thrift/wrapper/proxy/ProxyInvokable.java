package com.github.thrift.wrapper.proxy;

/**
 * Created by frank.li on 2017/4/20.
 */
public interface ProxyInvokable {

    /**
     * The interface to be proxied
     *
     * @return
     */
    Class<?> getProxyInterface();

    /**
     * The class of the real target to which the proxy methods will be delegated
     * This can be an interface or class, it will be used to determine the target object
     * Not required if the target already provided
     *
     * @return
     */
    Class<?> getTargetClass();

    /**
     * The real target to which the proxy methods will be delegated
     * Not required if it can be determined by the target class
     *
     * @return
     */
    public Object getTarget();

    /**
     * The ProxyFactory instance that will be used to create proxy instance
     * We provide 3 different ProxyFactory: Jdk, Cglib, Javassist
     *
     * @return
     */
    ProxyFactory getProxyFactory();

    /**
     * The exceptions that will be wrapped as RuntimeException if they are
     * thrown from the proxy methods
     *
     * @return
     */
    String[] getWrappedExceptions();

    /**
     * Returns the corresponding method name in target class against the provided interface method
     *
     * @param interfaceMethod
     * @return
     */
    String getTargetMethod(String interfaceMethod);
}
