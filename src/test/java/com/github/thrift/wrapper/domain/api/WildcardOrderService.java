package com.github.thrift.wrapper.domain.api;

import com.github.thrift.wrapper.domain.entity.Order;

import java.util.List;

/**
 * Created by frank.li on 2017/4/17.
 */
public interface WildcardOrderService<T extends Order> {

    void save(T order);

    void saveOrderList(List<T> orders);

    void saveOrderArray(T[] orders);

    List<? extends Order> findByOrderIds(Long[] orderIds);
}
