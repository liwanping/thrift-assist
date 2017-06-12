package com.github.thrift.wrapper.benchmark;

import com.github.thrift.wrapper.AbstractTestCase;
import com.github.thrift.wrapper.domain.thrift.ThriftOrder;
import com.github.thrift.wrapper.domain.thrift.ThriftOrderService;
import com.github.thrift.wrapper.utils.TestUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Date;

/**
 * Created by frank.li on 2017/4/1.
 */
public abstract class AbstractThriftServiceBenchmarkTest extends AbstractTestCase {

    protected static final Logger logger = LoggerFactory.getLogger(AbstractThriftServiceBenchmarkTest.class);

    protected static final long BENCHMARK_SIZE = 100L;

    @Autowired
    protected ThriftOrderService.Iface thriftOrderService;

    protected void benchmarkTest(String proxyType) throws Exception {

        long startTime = System.nanoTime();
        for (int i = 0; i < BENCHMARK_SIZE; i++) {
            doBenchmarkTask();
        }
        long elapsedTime = System.nanoTime() - startTime;
        logger.info("{} service benchmark test total elapsed time: {} ns", proxyType, elapsedTime);
        logger.info("{} service benchmark test average elapsed time: {} ns", proxyType, elapsedTime / BENCHMARK_SIZE);
    }

    protected void doBenchmarkTask() throws Exception {

        thriftOrderService.findByOrderId(123);
        thriftOrderService.findByOrderIds(Arrays.asList(123L, 345L, 456L, 789L));
        thriftOrderService.findByPayerId(567L);
        thriftOrderService.findByCreateTime(new Date().getTime());
        thriftOrderService.findByPayerIds(Arrays.asList(123L, 987L, 578L));
        ThriftOrder thriftOrder = TestUtils.createThriftOrder(123);
        thriftOrderService.save(RandomUtils.nextInt(), thriftOrder);
        thriftOrderService.saveAll(RandomUtils.nextInt(), Arrays.asList(thriftOrder));
    }
}
