package com.github.thrift.wrapper.proxy;

import com.github.thrift.wrapper.translator.TranslatorUtils;
import com.github.thrift.wrapper.utils.ReflectionUtils;
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

        Type[] paramTypes = method.getGenericParameterTypes();
        Type[] targetParamTypes = targetMethod.getGenericParameterTypes();
        if (paramTypes.length != targetParamTypes.length) {
            throw new RuntimeException("Method parameters size not matched");
        }

        logger.debug("Start to translate method parameters");
        Object[] targetArgs = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            targetArgs[i] = TranslatorUtils.translate(args[i], targetParamTypes[i]);
        }

        Object result = ReflectionUtils.invokeMethod(targetMethod, proxyInvokable.getTarget(), targetArgs);

        logger.debug("Start to translate method return value");
        return TranslatorUtils.translate(result, method.getGenericReturnType());
    }
}
