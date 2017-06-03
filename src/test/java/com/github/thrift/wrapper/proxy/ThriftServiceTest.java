package com.github.thrift.wrapper.proxy;

import com.github.thrift.wrapper.AbstractTestCase;
import com.github.thrift.wrapper.domain.thrift.ThriftOrder;
import com.github.thrift.wrapper.domain.thrift.ThriftOrderService;
import com.github.thrift.wrapper.utils.TestUtils;
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
@ContextConfiguration(locations = {"classpath*:config/spring/local/appcontext-thrift-wrapper-service.xml"})
public class ThriftServiceTest extends AbstractTestCase {

    @Autowired
    private ThriftOrderService.Iface thriftOrderService;

    @Test
    public void testFindByOrderId() throws Exception {

        ThriftOrder thriftOrder = thriftOrderService.findByOrderId(123);
        Assert.assertNotNull(thriftOrder);
        Assert.assertEquals(123, thriftOrder.getOrderId());
    }

    @Test
    public void testFindByOrderIds() throws Exception {

        List<Long> orderIds = Arrays.asList(123L, 456L, 789L);
        Map<Long, ThriftOrder> orderMap = thriftOrderService.findByOrderIds(orderIds);
        Assert.assertNotNull(orderMap);
        Assert.assertEquals(orderIds.size(), orderMap.size());
        for (Long orderId : orderIds) {
            ThriftOrder thriftOrder = orderMap.get(orderId);
            Assert.assertNotNull(thriftOrder);
            Assert.assertEquals(orderId.longValue(), thriftOrder.getOrderId());
        }
    }

    @Test
    public void testFindByPayerId() throws Exception {

        Long payerId = RandomUtils.nextLong();
        List<ThriftOrder> thriftOrders = thriftOrderService.findByPayerId(payerId);
        Assert.assertNotNull(thriftOrders);
        for (ThriftOrder thriftOrder : thriftOrders) {
            Assert.assertEquals(payerId.longValue(), thriftOrder.getPayerId());
        }
    }

    @Test
    public void testFindByCreateTime() throws Exception {

        Long createTime = new Date().getTime();
        List<ThriftOrder> thriftOrders = thriftOrderService.findByCreateTime(createTime);
        Assert.assertNotNull(thriftOrders);
        for (ThriftOrder thriftOrder : thriftOrders) {
            Assert.assertEquals(createTime.longValue(), thriftOrder.getCreateTime());
        }
    }

    @Test
    public void testFindByPayerIds() throws Exception {

        List<Long> payerIds = Arrays.asList(100L, 200L, 300L, 400L);
        Map<Long, List<ThriftOrder>> payerOrderMap = thriftOrderService.findByPayerIds(payerIds);
        Assert.assertNotNull(payerOrderMap);
        Assert.assertEquals(payerIds.size(), payerOrderMap.size());

        for (Map.Entry<Long, List<ThriftOrder>> entry : payerOrderMap.entrySet()) {
            Long payerId = entry.getKey();
            List<ThriftOrder> orders = entry.getValue();
            for (ThriftOrder order : orders) {
                Assert.assertEquals(payerId.longValue(), order.getPayerId());
            }
        }
    }

    @Test
    public void testSaveOrder() throws Exception {

        ThriftOrder thriftOrder = TestUtils.createThriftOrder(123);
        thriftOrderService.save(thriftOrder);
        thriftOrderService.saveAll(Arrays.asList(thriftOrder));
    }
}
