package com.github.thrift.wrapper.proxy;

import com.github.thrift.wrapper.AbstractTestCase;
import com.github.thrift.wrapper.domain.api.WildcardOrderService;
import com.github.thrift.wrapper.domain.entity.Order;
import com.github.thrift.wrapper.utils.TestUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by frank.li on 2017/4/17.
 */
@ContextConfiguration(locations = {"classpath*:config/spring/local/appcontext-thrift-wrapper-service.xml"})
public class WildcardOrderServiceTest extends AbstractTestCase {

    @Autowired
    private WildcardOrderService<Order> wildcardOrderService;

    @Test
    public void testSaveOrder() {

        Long[] orderIds = new Long[]{123L, 456L};
        List<Order> orders = new ArrayList<Order>(orderIds.length);
        for (Long orderId : orderIds) {
            Order order = TestUtils.createOrder(orderId);
            orders.add(order);
        }

        wildcardOrderService.save(orders.get(0));
        wildcardOrderService.saveOrderList(orders);
        wildcardOrderService.saveOrderArray(orders.toArray(new Order[0]));
    }

    @Test
    public void testFindByOrderIds() throws Exception {

        Long[] orderIds = new Long[]{123L, 456L, 789L};
        List<? extends Order> orders = wildcardOrderService.findByOrderIds(orderIds);
        Assert.assertNotNull(orders);
        Assert.assertEquals(orderIds.length, orders.size());
    }

}
