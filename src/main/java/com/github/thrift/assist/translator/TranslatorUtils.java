package com.github.thrift.assist.translator;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by frank.li on 2017/3/23.
 */
public class TranslatorUtils {

    public static <T> T translate(Object srcObj, Type destType) {
        return getTranslatorHandler().translate(srcObj, destType);
    }

    public static <T> T translate(Object srcObj, Class<T> destClass) {
        return getTranslatorHandler().translate(srcObj, destClass);
    }

    public static <T> List<T> translate(List<?> srcObjList, Class<T> destObjClass) {

        if (srcObjList == null || srcObjList.isEmpty()) {
            return new ArrayList<T>(0);
        }

        List<T> destObjList = new ArrayList<T>(srcObjList.size());
        for (Object srcObj : srcObjList) {
            destObjList.add(translate(srcObj, destObjClass));
        }

        return destObjList;
    }

    public static <K, V> Map<K, V> translate(Map<?, ?> srcObjMap, Class<K> destKeyClass, Class<V> destValueClass) {

        if (srcObjMap == null || srcObjMap.isEmpty()) {
            return new HashMap<K, V>(0);
        }

        Map<K, V> destObjMap = new HashMap<K, V>(srcObjMap.size());
        for (Map.Entry<?, ?> entry : srcObjMap.entrySet()) {
            destObjMap.put(translate(entry.getKey(), destKeyClass),
                    translate(entry.getValue(), destValueClass));
        }
        return destObjMap;
    }

    private static TranslatorHandler getTranslatorHandler() {
        return new TranslatorHandler();
    }
}
