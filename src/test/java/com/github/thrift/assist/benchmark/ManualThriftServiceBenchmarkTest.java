package com.github.thrift.assist.benchmark;

import com.github.thrift.assist.domain.api.OrderService;
import com.github.thrift.assist.domain.entity.Order;
import com.github.thrift.assist.domain.thrift.ThriftOrder;
import com.github.thrift.assist.domain.thrift.ThriftOrderService;
import com.github.thrift.assist.translator.TranslatorUtils;
import org.apache.thrift.TException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by frank.li on 2017/4/1.
 */
@ContextConfiguration(locations = {"classpath*:config/spring/local/appcontext-thrift-wrapper-service-manual.xml"})
public class ManualThriftServiceBenchmarkTest extends AbstractThriftServiceBenchmarkTest {

    @Test
    public void testManualBenchmark() throws Exception {
        benchmarkTest("MANUAL");
    }

    public static class ThriftOrderServiceImpl implements ThriftOrderService.Iface {

        @Autowired
        private OrderService orderService;

        @Override
        public void save(int routingId, ThriftOrder order) throws TException {
            orderService.save(TranslatorUtils.translate(order, Order.class));
        }

        @Override
        public void saveAll(int routingId, List<ThriftOrder> orders) throws TException {
            orderService.saveAll(TranslatorUtils.translate(orders, Order.class));
        }

        @Override
        public void deleteOrder(long orderId) throws TException {
            orderService.delete(orderId);
        }

        @Override
        public ThriftOrder findByOrderId(long orderId) throws TException {
            Order order = orderService.findByOrderId(orderId);
            return TranslatorUtils.translate(order, ThriftOrder.class);
        }

        @Override
        public List<ThriftOrder> findByPayerId(long payerId) throws TException {
            List<Order> orders = orderService.findByPayerId(payerId);
            return TranslatorUtils.translate(orders, ThriftOrder.class);
        }

        @Override
        public List<ThriftOrder> findByCreateTime(long createTime) throws TException {
            return TranslatorUtils.translate(orderService.findByCreateTime(new Date(createTime)), ThriftOrder.class);
        }

        @Override
        public Map<Long, ThriftOrder> findByOrderIds(List<Long> orderIds) throws TException {
            Map<Long, Order> orderMap = orderService.findByOrderIds(orderIds);
            Map<Long, ThriftOrder> result = new HashMap<Long, ThriftOrder>();
            for (Map.Entry<Long, Order> entry : orderMap.entrySet()) {
                result.put(entry.getKey(), TranslatorUtils.translate(entry.getValue(), ThriftOrder.class));
            }
            return result;
        }

        @Override
        public Map<Long, List<ThriftOrder>> findByPayerIds(List<Long> payerIds) throws TException {
            Map<Long, List<Order>> orderMap = orderService.findByPayerIds(payerIds);
            Map<Long, List<ThriftOrder>> result = new HashMap<Long, List<ThriftOrder>>();
            for (Map.Entry<Long, List<Order>> entry : orderMap.entrySet()) {
                result.put(entry.getKey(), TranslatorUtils.translate(entry.getValue(), ThriftOrder.class));
            }
            return result;
        }
    }
}
