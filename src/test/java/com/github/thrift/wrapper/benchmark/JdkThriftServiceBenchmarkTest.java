package com.github.thrift.wrapper.benchmark;

import com.github.thrift.wrapper.proxy.ProxyType;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

/**
 * Created by frank.li on 2017/4/1.
 */
@ContextConfiguration(locations = {"classpath*:config/spring/local/appcontext-thrift-wrapper-service-jdk.xml"})
public class JdkThriftServiceBenchmarkTest extends AbstractThriftServiceBenchmarkTest {

    @Test
    public void testJdkBenchmark() throws Exception {
        benchmarkTest(ProxyType.JDK.name());
    }
}
