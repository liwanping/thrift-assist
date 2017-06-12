package com.github.thrift.wrapper.domain.api;

import com.github.thrift.wrapper.annotation.Mapping;
import com.github.thrift.wrapper.domain.entity.Order;
import com.github.thrift.wrapper.domain.thrift.ThriftOrderService;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by frank.li on 2017/4/1.
 */
@Mapping(ThriftOrderService.class)
public interface OrderService {

    void save(Order order);

    void saveAll(List<Order> orders);

    @Mapping(method = "deleteOrder")
    void delete(long orderId);

    Order findByOrderId(long orderId);

    List<Order> findByPayerId(long payerId);

    List<Order> findByCreateTime(Date createTime);

    Map<Long, Order> findByOrderIds(List<Long> orderIds);

    Map<Long, List<Order>> findByPayerIds(List<Long> payerIds);
}
