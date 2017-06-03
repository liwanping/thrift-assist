package com.github.thrift.wrapper.translator;

import com.github.thrift.wrapper.spring.DozerContext;
import org.dozer.converters.PrimitiveOrWrapperConverter;
import org.dozer.util.CollectionUtils;

import java.lang.reflect.*;
import java.util.*;

/**
 * Created by frank.li on 2017/4/14.
 */
public class TranslatorHandler {

    private final PrimitiveOrWrapperConverter primitiveConverter = new PrimitiveOrWrapperConverter();

    @SuppressWarnings("unchecked")
    public <T> T translate(Object srcObj, Type destType) {

        if (srcObj == null) {
            return null;
        }

        if (destType instanceof Class) {
            return (T) translateObject(srcObj, (Class<?>) destType);
        }
        if (destType instanceof ParameterizedType) {
            return (T) translateParameterizedTypeObject(srcObj, (ParameterizedType) destType);
        }
        if (destType instanceof WildcardType) {
            return (T) translateWildcardTypeObject(srcObj, (WildcardType) destType);
        }
        if (destType instanceof GenericArrayType) {
            return (T) translateGenericArrayTypeObject(srcObj, (GenericArrayType) destType);
        }
        if (destType instanceof TypeVariable) {
            return (T) translateTypeVariableObject(srcObj, (TypeVariable) destType);
        }

        throw new RuntimeException("Unexpected error happened, should not arrive at this point!");
    }

    private Object translateObject(Object srcObj, Class<?> destObjClass) {

        if (primitiveConverter.accepts(destObjClass)) {
            return primitiveConverter.convert(srcObj, destObjClass, DozerContext.getDateFormatContainer());
        }
        if (CollectionUtils.isArray(destObjClass)) {
            return translateArray(srcObj, destObjClass.getComponentType());
        }

        Class<?> srcObjClass = srcObj.getClass();
        if (CollectionUtils.isArray(srcObjClass)) {
            return translateArray(srcObj, destObjClass);
        }
        if (CollectionUtils.isCollection(srcObjClass)) {
            return translateCollection(srcObj, srcObjClass, destObjClass);
        }

        return translateCustomObject(srcObj, destObjClass);
    }

    private Object translateParameterizedTypeObject(Object srcObj, ParameterizedType parameterizedType) {

        Type rawType = parameterizedType.getRawType();
        if (rawType instanceof Class) {
            Class<?> rawClass = (Class<?>) rawType;
            Type[] actualTypes = parameterizedType.getActualTypeArguments();
            if (CollectionUtils.isCollection(rawClass)) {
                return translateCollection(srcObj, rawClass, actualTypes[0]);
            } else if (Map.class.isAssignableFrom(rawClass)) {
                return translateMap(srcObj, rawClass, actualTypes);
            }
        }

        throw new IllegalStateException("Cannot determine how to translate this parameterized object!");
    }

    private Object translateCollection(Object srcObj, Class<?> destClass, Type destActualType) {

        Class<?> srcClass = srcObj.getClass();
        Collection srcCollection = null;
        if (CollectionUtils.isCollection(srcClass)) {
            srcCollection = (Collection) srcObj;
        } else if (CollectionUtils.isArray(srcClass)) {
            srcCollection = Arrays.asList((Object[]) srcObj);
        } else {
            throw new IllegalStateException("Cannot determine how to convert as a collection!");
        }

        Collection destCollection = (Collection) createParameterizedObject(destClass);
        for (Object srcElement : srcCollection) {
            Object destElement = translate(srcElement, destActualType);
            destCollection.add(destElement);
        }

        return destCollection;
    }

    private Object translateMap(Object srcObj, Class<?> destClass, Type[] destActualTypes) {

        Map scrMap = (Map) srcObj;
        Map destMap = (Map) createParameterizedObject(destClass);

        Iterator itr = scrMap.entrySet().iterator();
        while (itr.hasNext()) {
            Map.Entry entry = (Map.Entry) itr.next();
            Object destKey = translate(entry.getKey(), destActualTypes[0]);
            Object destVal = translate(entry.getValue(), destActualTypes[1]);
            destMap.put(destKey, destVal);
        }

        return destMap;
    }

    private Object translateArray(Object srcObj, Class<?> destComponentType) {

        Collection srcCollection = null;
        if (CollectionUtils.isCollection(srcObj.getClass())) {
            srcCollection = (Collection)srcObj;
        } else {
            srcCollection = Arrays.asList((Object[]) srcObj);
        }

        List translatedList = (List) translateCollection(srcObj, srcCollection.getClass(), destComponentType);
        Object result = CollectionUtils.convertListToArray(translatedList, destComponentType);
        return result;
    }

    private Object createParameterizedObject(Class<?> clazz) {

        if (Map.class.isAssignableFrom(clazz)) {
            return new HashMap();
        } else if (List.class.isAssignableFrom(clazz)) {
            return new ArrayList();
        } else if (Set.class.isAssignableFrom(clazz)) {
            return new HashSet();
        } else {
            throw new IllegalStateException("Type not expected: " + clazz);
        }
    }

    private Object translateWildcardTypeObject(Object srcObj, WildcardType destType) {

        Type actualType = null;
        Type[] upperBounds = destType.getUpperBounds();
        if (upperBounds != null && upperBounds.length > 0) {
            actualType = upperBounds[0];
        } else {
            Type[] lowerBounds = destType.getLowerBounds();
            if (lowerBounds != null && lowerBounds.length > 0) {
                actualType = lowerBounds[0];
            }
        }
        if (actualType != null) {
            return translate(srcObj, actualType);
        }
        throw new IllegalStateException("Type not expected: " + destType);
    }

    private Object translateTypeVariableObject(Object srcObj, TypeVariable destType) {

        Type[] bounds = destType.getBounds();
        if (bounds != null && bounds.length > 0) {
            return translate(srcObj, bounds[0]);
        }
        throw new IllegalStateException("Type not expected: " + destType);
    }

    private Object translateGenericArrayTypeObject(Object srcObj, GenericArrayType destType) {
        return translate(srcObj, destType.getGenericComponentType());
    }

    private <T> T translateCustomObject(Object srcObj, Class<T> destObjClass) {
        return DozerContext.getDozerMapper().map(srcObj, destObjClass);
    }
}
