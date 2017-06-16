package com.github.thrift.assist.benchmark;

import com.github.thrift.assist.proxy.ProxyType;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

/**
 * Created by frank.li on 2017/4/1.
 */
@ContextConfiguration(locations = {"classpath*:config/spring/local/appcontext-thrift-wrapper-service-cglib.xml"})
public class CglibThriftServiceBenchmarkTest extends AbstractThriftServiceBenchmarkTest {

    @Test
    public void testCglibBenchmark() throws Exception {
        benchmarkTest(ProxyType.CGLIB.name());
    }
}
