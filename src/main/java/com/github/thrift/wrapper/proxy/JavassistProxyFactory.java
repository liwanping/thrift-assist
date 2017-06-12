package com.github.thrift.wrapper.proxy;

import com.github.thrift.wrapper.translator.TranslatorUtils;
import com.github.thrift.wrapper.utils.ReflectionUtils;
import com.github.thrift.wrapper.utils.PrimitiveUtils;
import javassist.*;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by frank.li on 2017/4/18.
 */
public class JavassistProxyFactory implements ProxyFactory {

    private static final Map<String, Class> proxyCache = new ConcurrentHashMap<String, Class>();

    private static final String PROXY_CLASS_SUFFIX = "$javassist_proxy";
    private static final AtomicLong CLASS_NAME_COUNTER = new AtomicLong(0);

    @Override
    public Object getProxy(ProxyInvokable proxyInvokable) {

        String proxyInterfaceName = proxyInvokable.getProxyInterface().getName();
        Class proxyClass = proxyCache.get(proxyInterfaceName);
        if (proxyClass == null) {
            try {
                proxyClass = createJavassistByteCodeProxyClass(proxyInvokable);
                proxyCache.put(proxyInterfaceName, proxyClass);
            } catch (Exception e) {
                throw new RuntimeException("Failed to create javassist bytecode proxy: " + proxyInterfaceName, e);
            }
        }

        try {
            Object proxyInstance = proxyClass.newInstance();
            ReflectionUtils.invokeMethod("setProxyInvokable", proxyInstance, proxyInvokable);
            return proxyInstance;
        } catch (InstantiationException e) {
            throw new RuntimeException("Failed to initialize proxy: " + proxyClass.getName(), e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to initialize proxy: " + proxyClass.getName(), e);
        }
    }

    private Class createJavassistByteCodeProxyClass(ProxyInvokable proxyInvokable) throws Exception {

        Class<?> proxyInterface = proxyInvokable.getProxyInterface();
        Class<?> targetClass = proxyInvokable.getTargetClass();

        ClassPool pool = ClassPool.getDefault();
        // add the current classpath, class loading for web application required
        pool.insertClassPath(new ClassClassPath(this.getClass()));

        CtClass proxy = pool.makeClass(proxyInterface.getName() +
                PROXY_CLASS_SUFFIX + CLASS_NAME_COUNTER.getAndIncrement());
        proxy.addInterface(pool.get(proxyInterface.getName()));

        CtConstructor constructor = CtNewConstructor.defaultConstructor(proxy);
        proxy.addConstructor(constructor);

        CtField proxyInvokableField = CtField.make("private " + ProxyInvokable.class.getName() + " proxyInvokable;", proxy);
        proxy.addField(proxyInvokableField);

        CtMethod proxyInvokableSetter = CtNewMethod.make(generateProxyInvokableSetterCode(), proxy);
        proxy.addMethod(proxyInvokableSetter);

        CtClass ctProxyInterface = pool.get(proxyInterface.getName());
        CtClass ctTargetClass = pool.get(targetClass.getName());
        CtMethod[] ctMethods = ctProxyInterface.getDeclaredMethods();
        for (CtMethod ctMethod : ctMethods) {
            String targetMethodName = proxyInvokable.getTargetMethod(ctMethod.getName());
            CtMethod ctTargetMethod = ctTargetClass.getDeclaredMethod(targetMethodName);
            String methodBody = generateMethodBody(proxyInvokable, ctMethod, ctTargetMethod);
            CtMethod ctProxyMethod = CtNewMethod.make(Modifier.PUBLIC, ctMethod.getReturnType(),
                    ctMethod.getName(), ctMethod.getParameterTypes(), ctMethod.getExceptionTypes(), methodBody, proxy);
            wrapExceptions(proxyInvokable, pool, ctProxyMethod);
            proxy.addMethod(ctProxyMethod);
        }

        Class proxyClass = proxy.toClass();

        // detach all loaded classes to release the memory
        proxy.detach();
        ctProxyInterface.detach();
        ctTargetClass.detach();
        return proxyClass;
    }

    private String generateMethodBody(ProxyInvokable proxyInvokable, CtMethod method, CtMethod targetMethod) throws Exception {

        StringBuilder body = new StringBuilder("{");

        // fetch all target parameter types in advance
        body.append(Type.class.getName()).append("[] targetParameterTypes = ")
                .append(ReflectionUtils.class.getName())
                .append(".getMethodParameterTypes(proxyInvokable.getTargetClass(), \"")
                .append(targetMethod.getName()).append("\");");

        CtClass[] parameterTypes = method.getParameterTypes();
        CtClass[] targetParameterTypes = targetMethod.getParameterTypes();

        boolean requireReturnValue = !method.getReturnType().getName().equals("void");
        if (requireReturnValue) {
            body.append(targetMethod.getReturnType().getName()).append(" result = ");
        }

        // invoke target method
        body.append("((").append(proxyInvokable.getTargetClass().getName())
                .append(")proxyInvokable.getTarget()).").append(targetMethod.getName()).append("(");
        for (int i = 0; i < parameterTypes.length; i++) {
            if (i > 0) {
                body.append(",");
            }

            // force to cast as the target parameter type
            if (PrimitiveUtils.isPrimitive(targetParameterTypes[i])) {
                // parse primitive type or array
                body.append(PrimitiveUtils.class.getName()).append(".parse((");
                body.append(PrimitiveUtils.getWrapperName(targetParameterTypes[i])).append(")");
            } else {
                // force to cast since generic type erased as Object
                body.append("(").append(targetParameterTypes[i].getName()).append(")");
            }

            // invoke to translate parameter value into target type
            body.append(TranslatorUtils.class.getName()).append(".translate(($w)");
            body.append("$").append(i + 1);
            body.append(", ").append("targetParameterTypes[").append(i).append("])");
            if (PrimitiveUtils.isPrimitive(targetParameterTypes[i])) {
                body.append(")");
            }
        }
        body.append(");");

        if (requireReturnValue) {
            body.append(Type.class.getName()).append(" returnType = ")
                    .append(ReflectionUtils.class.getName())
                    .append(".getMethodReturnType(proxyInvokable.getProxyInterface(), \"")
                    .append(method.getName()).append("\");");

            // use $r to force cast since generic type erased as Object
            body.append("return ($r)").append(TranslatorUtils.class.getName()).append(".translate(");
            body.append("($w)result").append(", ").append("returnType");
            body.append(");");
        }

        body.append("}");
        return body.toString();
    }

    private String generateProxyInvokableSetterCode() {

        StringBuilder code = new StringBuilder("public void setProxyInvokable(");
        code.append(ProxyInvokable.class.getName())
                .append(" proxyInvokable) { this.proxyInvokable = $1; }");
        return code.toString();
    }

    private void wrapExceptions(ProxyInvokable proxyInvokable, ClassPool pool, CtMethod method) throws Exception {

        String[] wrappedExceptions = proxyInvokable.getWrappedExceptions();
        if (wrappedExceptions == null || wrappedExceptions.length < 1) {
            return;
        }

        for (String exception : wrappedExceptions) {
            CtClass exceptionType = pool.get(exception);
            StringBuilder body = new StringBuilder("{");
            body.append("throw new RuntimeException(\"Unexpected exception thrown: $e.getMessage()\", $e);");
            body.append("}");
            method.addCatch(body.toString(), exceptionType);
        }
    }
}
