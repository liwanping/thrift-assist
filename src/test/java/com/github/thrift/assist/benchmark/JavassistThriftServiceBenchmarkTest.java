package com.github.thrift.assist.benchmark;

import com.github.thrift.assist.proxy.ProxyType;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

/**
 * Created by frank.li on 2017/4/1.
 */
@ContextConfiguration(locations = {"classpath*:config/spring/local/appcontext-thrift-wrapper-service.xml"})
public class JavassistThriftServiceBenchmarkTest extends AbstractThriftServiceBenchmarkTest {

    @Test
    public void testJavassistBenchmark() throws Exception {
        benchmarkTest(ProxyType.JAVASSIST.name());
    }
}
