package com.github.thrift.assist.proxy;

import com.github.thrift.assist.AbstractTestCase;
import com.github.thrift.assist.domain.api.OrderService;
import com.github.thrift.assist.domain.entity.Order;
import com.github.thrift.assist.utils.TestUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by frank.li on 2017/4/1.
 */
@ContextConfiguration(locations = {"classpath*:config/spring/local/appcontext-thrift-wrapper-client.xml"})
public class ThriftServiceClientTest extends AbstractTestCase {

    @Autowired
    private OrderService orderService;

    @Test
    public void testFindOrderId() throws Exception {

        Order order = orderService.findByOrderId(123);
        Assert.assertNotNull(order);
        Assert.assertEquals(123, order.getOrderId().longValue());
    }

    @Test
    public void testFindByOrderIds() throws Exception {

        List<Long> orderIds = Arrays.asList(123L, 456L, 789L);
        Map<Long, Order> orderMap = orderService.findByOrderIds(orderIds);
        Assert.assertNotNull(orderMap);
        Assert.assertEquals(orderIds.size(), orderMap.size());
        for (Long orderId : orderIds) {
            Order order = orderMap.get(orderId);
            Assert.assertNotNull(order);
            Assert.assertEquals(orderId, order.getOrderId());
        }
    }

    @Test
    public void testFindByPayerId() throws Exception {

        Long payerId = RandomUtils.nextLong();
        List<Order> orders = orderService.findByPayerId(payerId);
        Assert.assertNotNull(orders);
        for (Order order : orders) {
            Assert.assertEquals(payerId, order.getPayerId());
        }
    }

    @Test
    public void testFindByCreateTime() throws Exception {

        Date createTime = new Date();
        List<Order> orders = orderService.findByCreateTime(createTime);
        Assert.assertNotNull(orders);
        for (Order order : orders) {
            Assert.assertEquals(createTime.getTime(), order.getCreateTime().getTime());
        }
    }

    @Test
    public void testFindByPayerIds() throws Exception {

        List<Long> payerIds = Arrays.asList(100L, 200L, 300L, 400L);
        Map<Long, List<Order>> payerOrderMap = orderService.findByPayerIds(payerIds);
        Assert.assertNotNull(payerOrderMap);
        Assert.assertEquals(payerIds.size(), payerOrderMap.size());

        for (Map.Entry<Long, List<Order>> entry : payerOrderMap.entrySet()) {
            Long payerId = entry.getKey();
            List<Order> orders = entry.getValue();
            for (Order order : orders) {
                Assert.assertEquals(payerId, order.getPayerId());
            }
        }
    }

    @Test
    public void testSaveOrder() throws Exception {

        Order order = TestUtils.createOrder(123);
        orderService.save(order);
        orderService.saveAll(Arrays.asList(order));
    }

    @Test
    public void testDeleteOrder() throws Exception {
        orderService.delete(123);
    }
}
