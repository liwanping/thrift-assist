package com.github.thrift.wrapper.utils;

import com.github.thrift.wrapper.domain.entity.Order;
import com.github.thrift.wrapper.domain.thrift.ThriftOrder;
import org.apache.commons.lang.math.RandomUtils;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by frank.li on 2017/4/1.
 */
public class TestUtils {

    public static Order createOrder() {
        return createOrder(RandomUtils.nextLong());
    }

    public static Order createOrder(long orderId) {
        Order order = new Order();
        order.setOrderId(orderId);
        order.setName("Order Test");
        order.setPayerId(RandomUtils.nextLong());
        order.setQuantity(RandomUtils.nextInt());
        order.setPrice(new BigDecimal(199.95));
        order.setCreateTime(new Date());
        return order;
    }

    public static ThriftOrder createThriftOrder() {
        return createThriftOrder(RandomUtils.nextLong());
    }

    public static ThriftOrder createThriftOrder(long orderId) {

        ThriftOrder thriftOrder = new ThriftOrder();
        thriftOrder.setOrderId(orderId);
        thriftOrder.setName("Order Test");
        thriftOrder.setPayerId(RandomUtils.nextLong());
        thriftOrder.setQuantity(RandomUtils.nextInt());
        thriftOrder.setPrice("199.95");
        thriftOrder.setCreateTime(new Date().getTime());
        return thriftOrder;
    }
}
