package com.ctrip.soa.caravan.hystrix.config;

import com.ctrip.soa.caravan.common.concurrent.collect.circularbuffer.timebucket.PercentileBufferConfig;
import com.ctrip.soa.caravan.common.concurrent.collect.circularbuffer.timebucket.TimeBufferConfig;
import com.ctrip.soa.caravan.common.value.checker.NullArgumentChecker;
import com.ctrip.soa.caravan.configuration.facade.TypedDynamicCachedCorrectedProperties;
import com.ctrip.soa.caravan.configuration.typed.TypedProperty;
import com.ctrip.soa.caravan.hystrix.ExecutionCommand;
import static com.ctrip.soa.caravan.hystrix.util.KeyManager.*;

import static com.ctrip.soa.caravan.configuration.util.PropertyValueGetter.*;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class DefaultMetricsConfig implements MetricsConfig {

    private TypedProperty<Long> _managerLevelHealthSnapshotIntervalProperty;
    private TypedProperty<Long> _groupLevelHealthSnapshotIntervalProperty;
    private TypedProperty<Long> _healthSnapshotIntervalProperty;

    private TimeBufferConfig _eventCounterConfig;
    private PercentileBufferConfig _executionLatencyConfig;

    public DefaultMetricsConfig(ExecutionCommand command, TypedDynamicCachedCorrectedProperties properties, TimeBufferConfig eventCounterConfig,
            PercentileBufferConfig executionLatencyConfig) {
        NullArgumentChecker.DEFAULT.check(command, "command");
        NullArgumentChecker.DEFAULT.check(properties, "properties");
        NullArgumentChecker.DEFAULT.check(eventCounterConfig, "eventCounterConfig");
        NullArgumentChecker.DEFAULT.check(executionLatencyConfig, "executionLatencyConfig");

        _managerLevelHealthSnapshotIntervalProperty = properties.getLongProperty(getManagerLevelPropertyKey(command, HEALTH_SNAPSHOT_INTERVAL_PROPERTY_KEY), 100L, 0,
                Long.MAX_VALUE);
        _groupLevelHealthSnapshotIntervalProperty = properties.getLongProperty(getGroupLevelPropertyKey(command, HEALTH_SNAPSHOT_INTERVAL_PROPERTY_KEY), null, 0,
                Long.MAX_VALUE);
        _healthSnapshotIntervalProperty = properties.getLongProperty(getCommandLevelPropertyKey(command, HEALTH_SNAPSHOT_INTERVAL_PROPERTY_KEY), null, 0, Long.MAX_VALUE);

        _eventCounterConfig = eventCounterConfig;
        _executionLatencyConfig = executionLatencyConfig;
    }

    @Override
    public TimeBufferConfig eventCounterConfig() {
        return _eventCounterConfig;
    }

    @Override
    public long healthSnapshotInterval() {
        return getTypedValue(_managerLevelHealthSnapshotIntervalProperty, _groupLevelHealthSnapshotIntervalProperty, _healthSnapshotIntervalProperty);
    }

    @Override
    public PercentileBufferConfig executionLatencyConfig() {
        return _executionLatencyConfig;
    }

}
