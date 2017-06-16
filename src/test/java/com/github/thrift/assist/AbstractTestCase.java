package com.github.thrift.assist;

import com.github.thrift.assist.domain.entity.Order;
import com.github.thrift.assist.domain.thrift.ThriftOrder;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by frank.li on 2017/3/31.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:config/spring/local/appcontext-thrift-wrapper.xml"})
public abstract class AbstractTestCase {

    protected void assertOrder(Order order, ThriftOrder thriftOrder) {

        Assert.assertEquals(order.getOrderId().longValue(), thriftOrder.getOrderId());
        Assert.assertEquals(order.getName(), thriftOrder.getName());
        Assert.assertEquals(order.getPayerId().longValue(), thriftOrder.getPayerId());
        Assert.assertEquals(order.getQuantity().longValue(), thriftOrder.getQuantity());
        Assert.assertEquals(order.getPrice().toString(), thriftOrder.getPrice());
        Assert.assertEquals(order.getCreateTime().getTime(), thriftOrder.getCreateTime());
    }
}
