package com.github.thrift.assist.translator;

import com.github.thrift.assist.AbstractTestCase;
import com.github.thrift.assist.domain.entity.Order;
import com.github.thrift.assist.domain.thrift.ThriftOrder;
import com.github.thrift.assist.utils.TestUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by frank.li on 2017/3/31.
 */
public class TranslatorUtilsTest extends AbstractTestCase {

    @Test
    public void testTranslateObject() {

        Order order = TestUtils.createOrder();

        ThriftOrder thriftOrder = TranslatorUtils.translate(order, ThriftOrder.class);
        assertOrder(order, thriftOrder);

        order = TranslatorUtils.translate(thriftOrder, Order.class);
        assertOrder(order, thriftOrder);
    }

    @Test
    public void testTranslateObjectList() {

        final int batch = 10;

        List<Order> orders = new ArrayList<Order>(batch);
        for (int i = 0; i < batch; i++) {
            orders.add(TestUtils.createOrder());
        }

        List<ThriftOrder> thriftOrders = TranslatorUtils.translate(orders, ThriftOrder.class);
        for (int i = 0; i < batch; i++) {
            assertOrder(orders.get(i), thriftOrders.get(i));
        }
    }

    @Test
    public void testTranslateObjectMap() {

        final int batch = 10;
        Map<Long, Order> orderMap = new HashMap<Long, Order>(batch);
        for (int i = 0; i < batch; i++) {
            Order order = TestUtils.createOrder();
            orderMap.put(order.getOrderId(), order);
        }

        Map<Long, ThriftOrder> thriftOrderMap = TranslatorUtils.translate(orderMap, Long.class, ThriftOrder.class);
        for (Map.Entry<Long, Order> orderEntry : orderMap.entrySet()) {
            Assert.assertTrue(thriftOrderMap.containsKey(orderEntry.getKey()));
            assertOrder(orderEntry.getValue(), thriftOrderMap.get(orderEntry.getKey()));
        }
    }

    @Test
    public void testTranslateObjectArray() {

        final int batch = 10;

        Order[] orders = new Order[batch];
        for (int i = 0; i < batch; i++) {
            orders[i] = TestUtils.createOrder();
        }

        ThriftOrder[] thriftOrders = TranslatorUtils.translate(orders, new ThriftOrder[0].getClass());
        for (int i = 0; i < batch; i++) {
            assertOrder(orders[i], thriftOrders[i]);
        }

        Long[] numbers = new Long[]{100L, 200L, 450L};
        Long[] translateNumbers = TranslatorUtils.translate(numbers, numbers.getClass());
        for (int i = 0; i < numbers.length; i++) {
            Assert.assertEquals(numbers[i], translateNumbers[i]);
        }
    }

}
