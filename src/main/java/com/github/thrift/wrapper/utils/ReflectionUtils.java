package com.github.thrift.wrapper.utils;

import org.springframework.util.ConcurrentReferenceHashMap;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.Map;

/**
 * Created by frank.li on 2017/4/14.
 */
public final class ReflectionUtils {

    private static final Map<Class<?>, Method[]> declaredMethodsCache = new ConcurrentReferenceHashMap(256);

    private static final Map<String, Type[]> methodParameterTypesCache = new ConcurrentReferenceHashMap(256);

    private static final Map<String, Type> methodReturnTypeCache = new ConcurrentReferenceHashMap(256);

    public static Method findMethod(Class<?> clazz, String methodName, Class... paramTypes) {

        for (Class searchType = clazz; searchType != null; searchType = searchType.getSuperclass()) {
            Method[] methods = searchType.isInterface() ? searchType.getMethods() : getDeclaredMethods(searchType);

            for (Method method : methods) {
                if (methodName.equals(method.getName()) &&
                        (paramTypes == null || Arrays.equals(paramTypes, method.getParameterTypes()))) {
                    return method;
                }
            }
        }

        return null;
    }

    public static Method findMethod(Class<?> clazz, String methodName) {
        return findMethod(clazz, methodName, null);
    }

    public static Object invokeMethod(Method method, Object target, Object... args) {
        try {
            return method.invoke(target, args);
        } catch (Exception e) {
            throw new RuntimeException("Invoke method failed: " + method.getName(), e);
        }
    }

    public static Object invokeMethod(String methodName, Object target, Object... args) {
        Method method = findMethod(target.getClass(), methodName);
        if (method == null) {
            throw new RuntimeException("Failed to find method: " + methodName + " for class: " + target.getClass().getName());
        }
        return invokeMethod(method, target, args);
    }

    public static Field findField(Class<?> clazz, String name) {
        return findField(clazz, name, (Class) null);
    }

    public static Field findField(Class<?> clazz, String name, Class<?> type) {

        for (Class searchType = clazz;
             !Object.class.equals(searchType) && searchType != null;
             searchType = searchType.getSuperclass()) {

            Field[] fields = searchType.getDeclaredFields();
            for (Field field : fields) {
                if ((name == null || name.equals(field.getName())) && (type == null || type.equals(field.getType()))) {
                    return field;
                }
            }
        }

        return null;
    }

    public static void setFieldValue(Field field, Object target, Object value) {
        try {
            field.set(target, value);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Unexpected reflection exception", e);
        }
    }

    public static Object getFieldValue(Field field, Object target) {
        try {
            return field.get(target);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Unexpected reflection exception", e);
        }
    }

    public static Object getFieldValue(Object target, String name) {
        Field field = findField(target.getClass(), name);
        if (field != null) {
            makeAccessible(field);
            return getFieldValue(field, target);
        }
        return null;
    }

    public static Type[] getMethodParameterTypes(Class<?> clazz, String methodName) {

        String methodKey = clazz.getName() + "." + methodName;
        Type[] parameterTypes = methodParameterTypesCache.get(methodKey);
        if (parameterTypes == null) {
            Method method = findMethod(clazz, methodName);
            if (method == null) {
                throw new RuntimeException("Failed to find method: " + methodName + " for class: " + clazz.getName());
            }

            parameterTypes = method.getGenericParameterTypes();
            methodParameterTypesCache.put(methodKey, parameterTypes);
        }

        return parameterTypes;
    }

    public static Type getMethodReturnType(Class<?> clazz, String methodName) {

        String methodKey = clazz.getName() + "." + methodName;
        Type returnType = methodReturnTypeCache.get(methodKey);
        if (returnType == null) {
            Method method = findMethod(clazz, methodName);
            if (method == null) {
                throw new RuntimeException("Failed to find method: " + methodName + " for class: " + clazz.getName());
            }

            returnType = method.getGenericReturnType();
            methodReturnTypeCache.put(methodKey, returnType);
        }

        return returnType;
    }

    public static void makeAccessible(Field field) {
        if ((!Modifier.isPublic(field.getModifiers()) ||
                !Modifier.isPublic(field.getDeclaringClass().getModifiers()) ||
                Modifier.isFinal(field.getModifiers())) && !field.isAccessible()) {
            field.setAccessible(true);
        }
    }

    public static void makeAccessible(Method method) {
        if ((!Modifier.isPublic(method.getModifiers()) ||
                !Modifier.isPublic(method.getDeclaringClass().getModifiers())) && !method.isAccessible()) {
            method.setAccessible(true);
        }
    }

    private static Method[] getDeclaredMethods(Class<?> clazz) {

        Method[] result = (Method[]) declaredMethodsCache.get(clazz);
        if (result == null) {
            result = clazz.getDeclaredMethods();
            declaredMethodsCache.put(clazz, result);
        }

        return result;
    }
}
