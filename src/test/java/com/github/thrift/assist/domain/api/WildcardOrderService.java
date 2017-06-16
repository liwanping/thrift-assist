package com.github.thrift.assist.domain.api;

import com.github.thrift.assist.domain.entity.Order;

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
