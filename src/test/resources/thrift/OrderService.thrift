namespace java com.github.thrift.wrapper.domain.thrift

include "Orders.thrift"

service ThriftOrderService {

    void save(1:Orders.ThriftOrder order);

    void saveAll(1:list<Orders.ThriftOrder> orders);

    Orders.ThriftOrder findByOrderId(1:i64 orderId);

    list<Orders.ThriftOrder> findByPayerId(1:i64 payerId);

    list<Orders.ThriftOrder> findByCreateTime(1:i64 createTime);

    map<i64, Orders.ThriftOrder> findByOrderIds(1:list<i64> orderIds);

    map<i64, list<Orders.ThriftOrder>> findByPayerIds(1:list<i64> payerIds);
}