package com.ctrip.soa.caravan.hystrix.util;

import com.ctrip.soa.caravan.common.concurrent.collect.circularbuffer.timebucket.PercentileBufferConfig;
import com.ctrip.soa.caravan.common.concurrent.collect.circularbuffer.timebucket.TimeBufferConfig;
import com.ctrip.soa.caravan.common.value.checker.NullArgumentChecker;
import com.ctrip.soa.caravan.configuration.facade.TypedDynamicCachedCorrectedProperties;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class ManagerConfig {

    private TypedDynamicCachedCorrectedProperties _properties;
    private TimeBufferConfig _eventCounterConfig;
    private PercentileBufferConfig _executionLatencyConfig;

    public ManagerConfig(TypedDynamicCachedCorrectedProperties properties, TimeBufferConfig eventCounterConfig, PercentileBufferConfig executionLatencyConfig) {
        NullArgumentChecker.DEFAULT.check(properties, "properties");
        NullArgumentChecker.DEFAULT.check(eventCounterConfig, "eventCounterConfig");
        NullArgumentChecker.DEFAULT.check(executionLatencyConfig, "executionLatencyConfig");

        _properties = properties;
        _eventCounterConfig = eventCounterConfig;
        _executionLatencyConfig = executionLatencyConfig;
    }

    public TypedDynamicCachedCorrectedProperties properties() {
        return _properties;
    }

    public TimeBufferConfig eventCounterConfig() {
        return _eventCounterConfig;
    }

    public PercentileBufferConfig executionLatencyConfig() {
        return _executionLatencyConfig;
    }

}
