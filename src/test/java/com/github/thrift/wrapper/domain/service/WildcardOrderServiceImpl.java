package com.github.thrift.wrapper.domain.service;

import com.github.thrift.wrapper.domain.api.WildcardOrderService;
import com.github.thrift.wrapper.domain.entity.Order;
import com.github.thrift.wrapper.utils.TestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by frank.li on 2017/4/17.
 */
public class WildcardOrderServiceImpl<T extends Order> implements WildcardOrderService<T> {

    private static final Logger logger = LoggerFactory.getLogger(WildcardOrderServiceImpl.class);

    @Override
    public void save(T order) {
        logger.info("Simulate to save order");
    }

    @Override
    public void saveOrderList(List<T> orders) {
        logger.info("Simulate to save order list");
    }

    @Override
    public void saveOrderArray(T[] orders) {
        logger.info("Simulate to save order array");
    }

    @Override
    public List<? extends Order> findByOrderIds(Long[] orderIds) {

        logger.info("Simulate to findByOrderIds: {}", orderIds);
        List<Order> orders = new ArrayList<Order>(orderIds.length);
        for (Long orderId : orderIds) {
            orders.add(TestUtils.createOrder(orderId));
        }
        return orders;
    }


}
