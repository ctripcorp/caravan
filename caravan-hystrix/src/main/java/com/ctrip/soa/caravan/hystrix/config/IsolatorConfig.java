package com.ctrip.soa.caravan.hystrix.config;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public interface IsolatorConfig {

    String MAX_CONCURRENT_COUNT_PROPERTY_KEY = "execution.isolator.max-concurrent-count";

    long maxConcurrentCount();

}
