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
public class DefaultIsolatorConfig implements IsolatorConfig {

    private TypedProperty<Long> _managerLevelMaxConcurrentCountProperty;
    private TypedProperty<Long> _groupLevelMaxConcurrentCountProperty;
    private TypedProperty<Long> _maxConcurrentCountProperty;

    public DefaultIsolatorConfig(ExecutionCommand command, TypedDynamicCachedCorrectedProperties properties) {
        NullArgumentChecker.DEFAULT.check(command, "command");
        NullArgumentChecker.DEFAULT.check(properties, "properties");

        _managerLevelMaxConcurrentCountProperty = properties.getLongProperty(getManagerLevelPropertyKey(command, MAX_CONCURRENT_COUNT_PROPERTY_KEY), 200L, 1, Long.MAX_VALUE);
        _groupLevelMaxConcurrentCountProperty = properties.getLongProperty(getGroupLevelPropertyKey(command, MAX_CONCURRENT_COUNT_PROPERTY_KEY), null, 1, Long.MAX_VALUE);
        _maxConcurrentCountProperty = properties.getLongProperty(getCommandLevelPropertyKey(command, MAX_CONCURRENT_COUNT_PROPERTY_KEY), null, 1, Long.MAX_VALUE);
    }

    @Override
    public long maxConcurrentCount() {
        return getTypedValue(_managerLevelMaxConcurrentCountProperty, _groupLevelMaxConcurrentCountProperty, _maxConcurrentCountProperty);
    }

}
