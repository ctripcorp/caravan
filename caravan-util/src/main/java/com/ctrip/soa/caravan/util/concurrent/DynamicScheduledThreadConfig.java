package com.ctrip.soa.caravan.util.concurrent;

import com.ctrip.soa.caravan.common.value.checker.NullArgumentChecker;
import com.ctrip.soa.caravan.configuration.facade.TypedDynamicCachedCorrectedProperties;
import com.ctrip.soa.caravan.configuration.util.RangePropertyConfig;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class DynamicScheduledThreadConfig {

    public static final String INIT_DELAY_PROPERTY_KEY = "dynamic-scheduled-thread.init-delay";
    public static final String RUN_INTERVAL_PROPERTY_KEY = "dynamic-scheduled-thread.run-interval";

    private TypedDynamicCachedCorrectedProperties _properties;
    private RangePropertyConfig<Integer> _initDelayRange;
    private RangePropertyConfig<Integer> _runIntervalRange;

    public DynamicScheduledThreadConfig(TypedDynamicCachedCorrectedProperties properties, RangePropertyConfig<Integer> initDelayRange,
            RangePropertyConfig<Integer> runIntervalRange) {
        NullArgumentChecker.DEFAULT.check(properties, "properties");
        NullArgumentChecker.DEFAULT.check(initDelayRange, "initDelayRange");
        NullArgumentChecker.DEFAULT.check(initDelayRange.defaultValue(), "initDelayRange.defaultValue");
        NullArgumentChecker.DEFAULT.check(runIntervalRange, "runIntervalRange");
        NullArgumentChecker.DEFAULT.check(runIntervalRange.defaultValue(), "runIntervalRange.defaultValue");

        _properties = properties;
        _initDelayRange = initDelayRange;
        _runIntervalRange = runIntervalRange;
    }

    public TypedDynamicCachedCorrectedProperties properties() {
        return _properties;
    }

    public RangePropertyConfig<Integer> initDelayRange() {
        return _initDelayRange;
    }

    public RangePropertyConfig<Integer> runIntervalRange() {
        return _runIntervalRange;
    }

}
