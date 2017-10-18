package com.ctrip.soa.caravan.hystrix.config;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public interface CircuitBreakerConfig {

    String ENABLED_PROPERTY_KEY = "execution.circuit-breaker.enabled";
    String FORCE_OPEN_PROPERTY_KEY = "execution.circuit-breaker.force-open";
    String FORCE_CLOSED_PROPERTY_KEY = "execution.circuit-breaker.force-closed";
    String EXECUTION_TIMEOUT_PROPERTY_KEY = "execution.circuit-breaker.execution-timeout";
    String EXECUTION_COUNT_THRESHOLD_PROPERTY_KEY = "execution.circuit-breaker.execution-count-threshold";
    String ERROR_PERCENTAGE_THRESHOLD_PROPERTY_KEY = "execution.circuit-breaker.error-percentage-threshold";
    String RETRY_INTERVAL_PROPERTY_KEY = "execution.circuit-breaker.retry-interval";

    boolean enabled();

    boolean forceOpen();

    boolean forceClosed();

    long executionTimeout();

    long executionCountThreshold();

    int errorPercentageThreshold();

    long retryInterval();

}
