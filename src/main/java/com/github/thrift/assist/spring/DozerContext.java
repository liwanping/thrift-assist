package com.github.thrift.assist.spring;

import com.github.thrift.assist.utils.ReflectionUtils;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.dozer.classmap.Configuration;
import org.dozer.converters.DateFormatContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Method;

/**
 * Created by frank.li on 2017/4/16.
 */
public final class DozerContext implements ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(DozerContext.class);

    private static ApplicationContext appCtx;

    private static Mapper mapper;
    private static Configuration globalConfiguration;

    public static Mapper getDozerMapper() {
        initDozerMappings();
        return mapper;
    }

    public static DateFormatContainer getDateFormatContainer() {
        initDozerMappings();
        return new DateFormatContainer(globalConfiguration == null ? null : globalConfiguration.getDateFormat());
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        appCtx = applicationContext;
    }

    private static void initDozerMappings() {

        if (mapper != null) {
            // should be already initialized
            return;
        }
        if (appCtx != null) {
            logger.info("Initializing dozer mapper...");
            mapper = appCtx.getBean(Mapper.class);
        }
        if (mapper == null) {
            logger.warn("Dozer mapper not initialized with spring, use the default mapper!");
            mapper = new DozerBeanMapper();
        }

        // force init dozer mapping/configurations
        Method initMethod = ReflectionUtils.findMethod(mapper.getClass(), "initMappings");
        ReflectionUtils.makeAccessible(initMethod);
        ReflectionUtils.invokeMethod(initMethod, mapper);

        //customMappings = (ClassMappings) ReflectionUtils.getFieldValue(mapper, "customMappings");
        globalConfiguration = (Configuration) ReflectionUtils.getFieldValue(mapper, "globalConfiguration");
    }
}
