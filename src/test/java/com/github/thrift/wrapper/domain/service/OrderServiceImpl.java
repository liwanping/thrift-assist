package com.github.thrift.wrapper.domain.service;

import com.github.thrift.wrapper.domain.api.OrderService;
import com.github.thrift.wrapper.domain.entity.Order;
import com.github.thrift.wrapper.utils.TestUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by frank.li on 2017/4/1.
 */
public class OrderServiceImpl implements OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Override
    public void save(Order order) {
        logger.info("Simulate to save order: {}", order);
    }

    @Override
    public void saveAll(List<Order> orders) {
        logger.info("Simulate to save all orders: {}", orders);
    }

    @Override
    public Order findByOrderId(long orderId) {
        logger.info("Simulate to findByOrderId: {}", orderId);
        return TestUtils.createOrder(orderId);
    }

    @Override
    public List<Order> findByPayerId(long payerId) {

        logger.info("Simulate to findByPayerId: {}", payerId);

        List<Order> orders = new ArrayList<Order>();
        for (int i = 0; i < 3; i++) {
            Order order = TestUtils.createOrder();
            order.setPayerId(payerId);
            orders.add(order);
        }
        return orders;
    }

    @Override
    public List<Order> findByCreateTime(Date createTime) {

        logger.info("Simulate to findByCreateTime: {}", createTime);

        List<Order> orders = new ArrayList<Order>();
        for (int i = 0; i < 5; i++) {
            Order order = TestUtils.createOrder();
            order.setCreateTime(createTime);
            orders.add(order);
        }
        return orders;
    }


    @Override
    public Map<Long, Order> findByOrderIds(List<Long> orderIds) {

        logger.info("Simulate to findByOrderIds: {}", orderIds);

        Map<Long, Order> orders = new HashMap<Long, Order>();
        for (Long orderId : orderIds) {
            Order order = TestUtils.createOrder(orderId);
            orders.put(orderId, order);
        }
        return orders;
    }

    @Override
    public Map<Long, List<Order>> findByPayerIds(List<Long> payerIds) {


        logger.info("Simulate to findByPayerIds: {}", payerIds);

        Map<Long, List<Order>> result = new HashMap<Long, List<Order>>();
        for (Long payerId : payerIds) {
            int orderSize = Math.max(1, RandomUtils.nextInt() % 5);
            List<Order> orders = new ArrayList<Order>(orderSize);
            for (int i = 0; i < orderSize; i++) {
                Order order = TestUtils.createOrder();
                order.setPayerId(payerId);
                orders.add(order);
            }
            result.put(payerId, orders);
        }
        return result;
    }

}
