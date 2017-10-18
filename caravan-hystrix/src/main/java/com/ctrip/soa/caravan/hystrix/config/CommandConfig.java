package com.ctrip.soa.caravan.hystrix.config;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public interface CommandConfig {

    MetricsConfig metricsConfig();

    CircuitBreakerConfig circuitBreakerConfig();

    IsolatorConfig isolatorConfig();

}
