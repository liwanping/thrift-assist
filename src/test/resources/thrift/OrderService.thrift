namespace java com.github.thrift.assist.domain.thrift

include "Orders.thrift"

service ThriftOrderService {

    void save(1:i32 routingId, 2:Orders.ThriftOrder order);

    void saveAll(1:i32 routingId, 2:list<Orders.ThriftOrder> orders);

    void deleteOrder(1:i64 orderId);

    Orders.ThriftOrder findByOrderId(1:i64 orderId);

    list<Orders.ThriftOrder> findByPayerId(1:i64 payerId);

    list<Orders.ThriftOrder> findByCreateTime(1:i64 createTime);

    map<i64, Orders.ThriftOrder> findByOrderIds(1:list<i64> orderIds);

    map<i64, list<Orders.ThriftOrder>> findByPayerIds(1:list<i64> payerIds);
}