package com.ctrip.soa.caravan.hystrix.config;

import com.ctrip.soa.caravan.common.value.checker.NullArgumentChecker;
import com.ctrip.soa.caravan.configuration.facade.TypedDynamicCachedCorrectedProperties;
import com.ctrip.soa.caravan.configuration.typed.TypedProperty;
import com.ctrip.soa.caravan.hystrix.ExecutionCommand;
import static com.ctrip.soa.caravan.hystrix.util.KeyManager.*;
import static com.ctrip.soa.caravan.configuration.util.PropertyValueGetter.*;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class DefaultCircuitBreakerConfig implements CircuitBreakerConfig {

    private TypedProperty<Boolean> _managerLevelEnabledProperty;
    private TypedProperty<Boolean> _managerLevelForceOpenProperty;
    private TypedProperty<Boolean> _managerLevelForceClosedProperty;
    private TypedProperty<Long> _managerLevelExecutionTimeoutProperty;
    private TypedProperty<Long> _managerLevelExecutionCountThresholdProperty;
    private TypedProperty<Integer> _managerLevelErrorPercentageThresholdProperty;
    private TypedProperty<Long> _managerLevelRetryIntervalProperty;

    private TypedProperty<Boolean> _groupLevelEnabledProperty;
    private TypedProperty<Boolean> _groupLevelForceOpenProperty;
    private TypedProperty<Boolean> _groupLevelForceClosedProperty;
    private TypedProperty<Long> _groupLevelExecutionTimeoutProperty;
    private TypedProperty<Long> _groupLevelExecutionCountThresholdProperty;
    private TypedProperty<Integer> _groupLevelErrorPercentageThresholdProperty;
    private TypedProperty<Long> _groupLevelRetryIntervalProperty;

    private TypedProperty<Boolean> _enabledProperty;
    private TypedProperty<Boolean> _forceOpenProperty;
    private TypedProperty<Boolean> _forceClosedProperty;
    private TypedProperty<Long> _executionTimeoutProperty;
    private TypedProperty<Long> _executionCountThresholdProperty;
    private TypedProperty<Integer> _errorPercentageThresholdProperty;
    private TypedProperty<Long> _retryIntervalProperty;

    public DefaultCircuitBreakerConfig(ExecutionCommand command, TypedDynamicCachedCorrectedProperties properties) {
        NullArgumentChecker.DEFAULT.check(command, "command");
        NullArgumentChecker.DEFAULT.check(properties, "properties");

        _managerLevelEnabledProperty = properties.getBooleanProperty(getManagerLevelPropertyKey(command, ENABLED_PROPERTY_KEY), true);
        _managerLevelForceOpenProperty = properties.getBooleanProperty(getManagerLevelPropertyKey(command, FORCE_OPEN_PROPERTY_KEY), false);
        _managerLevelForceClosedProperty = properties.getBooleanProperty(getManagerLevelPropertyKey(command, FORCE_CLOSED_PROPERTY_KEY), false);
        _managerLevelExecutionTimeoutProperty = properties.getLongProperty(getManagerLevelPropertyKey(command, EXECUTION_TIMEOUT_PROPERTY_KEY), 20 * 1000L, 1,
                Long.MAX_VALUE);
        _managerLevelExecutionCountThresholdProperty = properties.getLongProperty(getManagerLevelPropertyKey(command, EXECUTION_COUNT_THRESHOLD_PROPERTY_KEY),
                20L, 1, Long.MAX_VALUE);
        _managerLevelErrorPercentageThresholdProperty = properties.getIntProperty(getManagerLevelPropertyKey(command, ERROR_PERCENTAGE_THRESHOLD_PROPERTY_KEY),
                50, 1, 100);
        _managerLevelRetryIntervalProperty = properties.getLongProperty(getManagerLevelPropertyKey(command, RETRY_INTERVAL_PROPERTY_KEY), 5 * 1000L, 1,
                Long.MAX_VALUE);

        _groupLevelEnabledProperty = properties.getBooleanProperty(getGroupLevelPropertyKey(command, ENABLED_PROPERTY_KEY));
        _groupLevelForceOpenProperty = properties.getBooleanProperty(getGroupLevelPropertyKey(command, FORCE_OPEN_PROPERTY_KEY));
        _groupLevelForceClosedProperty = properties.getBooleanProperty(getGroupLevelPropertyKey(command, FORCE_CLOSED_PROPERTY_KEY));
        _groupLevelExecutionTimeoutProperty = properties.getLongProperty(getGroupLevelPropertyKey(command, EXECUTION_TIMEOUT_PROPERTY_KEY), null, 1,
                Long.MAX_VALUE);
        _groupLevelExecutionCountThresholdProperty = properties.getLongProperty(getGroupLevelPropertyKey(command, EXECUTION_COUNT_THRESHOLD_PROPERTY_KEY), null,
                1, Long.MAX_VALUE);
        _groupLevelErrorPercentageThresholdProperty = properties.getIntProperty(getGroupLevelPropertyKey(command, ERROR_PERCENTAGE_THRESHOLD_PROPERTY_KEY), null,
                1, 100);
        _groupLevelRetryIntervalProperty = properties.getLongProperty(getGroupLevelPropertyKey(command, RETRY_INTERVAL_PROPERTY_KEY), null, 1,
                Long.MAX_VALUE);

        _enabledProperty = properties.getBooleanProperty(getCommandLevelPropertyKey(command, ENABLED_PROPERTY_KEY));
        _forceOpenProperty = properties.getBooleanProperty(getCommandLevelPropertyKey(command, FORCE_OPEN_PROPERTY_KEY));
        _forceClosedProperty = properties.getBooleanProperty(getCommandLevelPropertyKey(command, FORCE_CLOSED_PROPERTY_KEY));
        _executionTimeoutProperty = properties.getLongProperty(getCommandLevelPropertyKey(command, EXECUTION_TIMEOUT_PROPERTY_KEY), null, 1, Long.MAX_VALUE);
        _executionCountThresholdProperty = properties.getLongProperty(getCommandLevelPropertyKey(command, EXECUTION_COUNT_THRESHOLD_PROPERTY_KEY), null, 1,
                Long.MAX_VALUE);
        _errorPercentageThresholdProperty = properties.getIntProperty(getCommandLevelPropertyKey(command, ERROR_PERCENTAGE_THRESHOLD_PROPERTY_KEY), null, 1,
                100);
        _retryIntervalProperty = properties.getLongProperty(getCommandLevelPropertyKey(command, RETRY_INTERVAL_PROPERTY_KEY), null, 1, Long.MAX_VALUE);
    }

    @Override
    public boolean enabled() {
        return getTypedValue(_managerLevelEnabledProperty, _groupLevelEnabledProperty, _enabledProperty);
    }

    @Override
    public boolean forceOpen() {
        return getTypedValue(_managerLevelForceOpenProperty, _groupLevelForceOpenProperty, _forceOpenProperty);
    }

    @Override
    public boolean forceClosed() {
        return getTypedValue(_managerLevelForceClosedProperty, _groupLevelForceClosedProperty, _forceClosedProperty);
    }

    @Override
    public long executionTimeout() {
        return getTypedValue(_managerLevelExecutionTimeoutProperty, _groupLevelExecutionTimeoutProperty, _executionTimeoutProperty);
    }

    @Override
    public long executionCountThreshold() {
        return getTypedValue(_managerLevelExecutionCountThresholdProperty, _groupLevelExecutionCountThresholdProperty, _executionCountThresholdProperty);
    }

    @Override
    public int errorPercentageThreshold() {
        return getTypedValue(_managerLevelErrorPercentageThresholdProperty, _groupLevelErrorPercentageThresholdProperty, _errorPercentageThresholdProperty);
    }

    @Override
    public long retryInterval() {
        return getTypedValue(_managerLevelRetryIntervalProperty, _groupLevelRetryIntervalProperty, _retryIntervalProperty);
    }

}
