package com.github.thrift.assist.utils;

import javassist.CtClass;
import javassist.CtPrimitiveType;
import javassist.NotFoundException;

/**
 * Created by frank.li on 2017/4/18.
 */
public final class PrimitiveUtils {

    public static boolean isPrimitive(CtClass ctClass) {
        if (ctClass.isArray()) {
            try {
                return isPrimitive(ctClass.getComponentType());
            } catch (NotFoundException e) {
                return false;
            }
        }
        return ctClass.isPrimitive();
    }

    public static String getWrapperName(CtClass ctClass) {
        if (!isPrimitive(ctClass)) {
            return ctClass.getName();
        }
        if (ctClass.isArray()) {
            try {
                return getWrapperName(ctClass.getComponentType()) + "[]";
            } catch (NotFoundException e) {
            }
        }
        return ((CtPrimitiveType)ctClass).getWrapperName();
    }



    public static long parse(Long value) {
        if (value == null) {
            return 0L;
        }
        return value.longValue();
    }

    public static int parse(Integer value) {
        if (value == null) {
            return 0;
        }
        return value.intValue();
    }

    public static short parse(Short value) {
        if (value == null) {
            return 0;
        }
        return value.shortValue();
    }

    public static double parse(Double value) {
        if (value == null) {
            return 0d;
        }
        return value.doubleValue();
    }

    public static float parse(Float value) {
        if (value == null) {
            return 0f;
        }
        return value.floatValue();
    }

    public static boolean parse(Boolean value) {
        if (value == null) {
            return false;
        }
        return value.booleanValue();
    }

    public static byte parse(Byte value) {
        if (value == null) {
            return 0;
        }
        return value.byteValue();
    }

    public static char parse(Character value) {
        return value.charValue();
    }

    public static Long valueOf(long value) {
        return Long.valueOf(value);
    }

    public static Long[] valueOf(long[] values) {
        if (values == null) {
            return null;
        }
        Long[] result = new Long[values.length];
        for (int i = 0; i < values.length; i++) {
            result[i] = valueOf(values[i]);
        }
        return result;
    }

    public static long[] parse(Long[] values) {
        if (values == null) {
            return null;
        }
        long[] result = new long[values.length];
        for (int i = 0; i < values.length; i++) {
            result[i] = parse(values[i]);
        }
        return result;
    }

    public static int[] parse(Integer[] values) {
        if (values == null) {
            return null;
        }
        int[] result = new int[values.length];
        for (int i = 0; i < values.length; i++) {
            result[i] = parse(values[i]);
        }
        return result;
    }

    public static short[] parse(Short[] values) {
        if (values == null) {
            return null;
        }
        short[] result = new short[values.length];
        for (int i = 0; i < values.length; i++) {
            result[i] = parse(values[i]);
        }
        return result;
    }

    public static double[] parse(Double[] values) {
        if (values == null) {
            return null;
        }
        double[] result = new double[values.length];
        for (int i = 0; i < values.length; i++) {
            result[i] = parse(values[i]);
        }
        return result;
    }

    public static float[] parse(Float[] values) {
        if (values == null) {
            return null;
        }
        float[] result = new float[values.length];
        for (int i = 0; i < values.length; i++) {
            result[i] = parse(values[i]);
        }
        return result;
    }

    public static boolean[] parse(Boolean[] values) {
        if (values == null) {
            return null;
        }
        boolean[] result = new boolean[values.length];
        for (int i = 0; i < values.length; i++) {
            result[i] = parse(values[i]);
        }
        return result;
    }

    public static byte[] parse(Byte[] values) {
        if (values == null) {
            return null;
        }
        byte[] result = new byte[values.length];
        for (int i = 0; i < values.length; i++) {
            result[i] = parse(values[i]);
        }
        return result;
    }

    public static char[] parse(Character[] values) {
        if (values == null) {
            return null;
        }
        char[] result = new char[values.length];
        for (int i = 0; i < values.length; i++) {
            result[i] = parse(values[i]);
        }
        return result;
    }
}