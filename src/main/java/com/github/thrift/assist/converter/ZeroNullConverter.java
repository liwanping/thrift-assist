package com.github.thrift.assist.converter;

import org.dozer.DozerConverter;

/**
 * Created by frank.li on 2017/3/28.
 */
public class ZeroNullConverter extends DozerConverter<Number, Number> {

    public ZeroNullConverter() {
        super(Number.class, Number.class);
    }

    @Override
    public Number convertTo(Number a, Number b) {
        return convert(a, b);
    }

    @Override
    public Number convertFrom(Number a, Number b) {
        return convert(a, b);
    }

    private Number convert(Number a, Number b) {
        if (a == null) {
            return 0;
        }
        if (a.intValue() == 0) {
            return null;
        }
        return a;
    }

}
