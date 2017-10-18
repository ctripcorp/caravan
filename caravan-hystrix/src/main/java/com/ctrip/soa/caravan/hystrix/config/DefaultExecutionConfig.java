package com.ctrip.soa.caravan.hystrix.config;

import com.ctrip.soa.caravan.common.concurrent.collect.circularbuffer.timebucket.PercentileBufferConfig;
import com.ctrip.soa.caravan.common.concurrent.collect.circularbuffer.timebucket.TimeBufferConfig;
import com.ctrip.soa.caravan.common.value.checker.NullArgumentChecker;
import com.ctrip.soa.caravan.configuration.facade.TypedDynamicCachedCorrectedProperties;
import com.ctrip.soa.caravan.hystrix.ExecutionCommand;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class DefaultExecutionConfig implements CommandConfig {

    private MetricsConfig _metricsConfig;
    private CircuitBreakerConfig _circuitBreakerConfig;
    private IsolatorConfig _isolatorConfig;

    public DefaultExecutionConfig(ExecutionCommand command, TypedDynamicCachedCorrectedProperties properties,
            TimeBufferConfig eventCounterConfig, PercentileBufferConfig executionLatencyConfig) {
        NullArgumentChecker.DEFAULT.check(command, "command");
        NullArgumentChecker.DEFAULT.check(properties, "properties");

        _metricsConfig = new DefaultMetricsConfig(command, properties, eventCounterConfig, executionLatencyConfig);
        _circuitBreakerConfig = new DefaultCircuitBreakerConfig(command, properties);
        _isolatorConfig = new DefaultIsolatorConfig(command, properties);
    }

    @Override
    public MetricsConfig metricsConfig() {
        return _metricsConfig;
    }

    @Override
    public CircuitBreakerConfig circuitBreakerConfig() {
        return _circuitBreakerConfig;
    }

    @Override
    public IsolatorConfig isolatorConfig() {
        return _isolatorConfig;
    }

}
