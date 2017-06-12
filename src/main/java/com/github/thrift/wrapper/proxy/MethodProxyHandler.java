package com.github.thrift.wrapper.proxy;

import com.github.thrift.wrapper.translator.TranslatorUtils;
import com.github.thrift.wrapper.utils.ReflectionUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * Created by frank.li on 2017/4/1.
 */
public final class MethodProxyHandler {

    private static final Logger logger = LoggerFactory.getLogger(MethodProxyHandler.class);

    private static final MethodProxyHandler INSTANCE = new MethodProxyHandler();

    private MethodProxyHandler() {
    }

    public static MethodProxyHandler getInstance() {
        return INSTANCE;
    }

    public Object invoke(ProxyInvokable proxyInvokable, Method method, Object[] args) throws Throwable {

        try {
            return doInvoke(proxyInvokable, method, args);
        } catch (Throwable t) {
            String[] wrappedExceptions = proxyInvokable.getWrappedExceptions();
            if (wrappedExceptions == null || wrappedExceptions.length < 1) {
                throw t;
            }

            for (String wrappedException : wrappedExceptions) {
                Class<?> wrappedExceptionClass = Class.forName(wrappedException);
                if (wrappedExceptionClass.isAssignableFrom(t.getClass())) {
                    throw new RuntimeException("Unexpected exception thrown: " + t.getMessage(), t);
                }
            }
            throw t;
        }
    }

    private Object doInvoke(ProxyInvokable proxyInvokable, Method method, Object[] args) {

        logger.debug("Start to map method automatically");

        Method targetMethod = ReflectionUtils.findMethod(proxyInvokable.getTargetClass(),
                proxyInvokable.getTargetMethod(method.getName()));
        if (targetMethod == null) {
            throw new RuntimeException(String.format("Method[%s] not found for targetClass:[%s]",
                    method.getName(), proxyInvokable.getTargetClass().getName()));
        }

        Type[] parameterTypes = method.getGenericParameterTypes();
        Type[] targetParameterTypes = targetMethod.getGenericParameterTypes();

        boolean autoGenerateFirstArg = false;
        if (parameterTypes.length != targetParameterTypes.length) {
            autoGenerateFirstArg = true;
        }

        logger.debug("Start to translate method parameters");
        Object[] targetArgs = new Object[targetParameterTypes.length];
        for (int i = 0, j = 0; i < parameterTypes.length && j < targetParameterTypes.length; i++, j++) {
            if (autoGenerateFirstArg) {
                autoGenerateFirstArg = false;
                if (parameterTypes.length > targetParameterTypes.length) {
                    i++;
                } else {
                    targetArgs[j] = TranslatorUtils.translate(RandomUtils.nextInt(), targetParameterTypes[j]);
                    j++;
                }
            }

            targetArgs[j] = TranslatorUtils.translate(args[i], targetParameterTypes[j]);
        }

        Object result = ReflectionUtils.invokeMethod(targetMethod, proxyInvokable.getTarget(), targetArgs);

        logger.debug("Start to translate method return value");
        return TranslatorUtils.translate(result, method.getGenericReturnType());
    }
}
