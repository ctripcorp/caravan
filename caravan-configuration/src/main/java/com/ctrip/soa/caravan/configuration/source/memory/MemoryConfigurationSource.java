package com.ctrip.soa.caravan.configuration.source.memory;

import com.ctrip.soa.caravan.common.value.checker.NullArgumentChecker;
import com.ctrip.soa.caravan.configuration.AbstractConfigurationSource;
import com.ctrip.soa.caravan.configuration.dynamic.ConfigurationSourceChangeListener;
import com.ctrip.soa.caravan.configuration.dynamic.DynamicConfigurationSource;

/**
 * Created by w.jian on 2016/5/18.
 */
public class MemoryConfigurationSource extends AbstractConfigurationSource implements DynamicConfigurationSource {

    private final MemoryConfiguration _memoryConfiguration;

    public MemoryConfigurationSource(int priority, String sourceId) {
        super(priority, sourceId);
        _memoryConfiguration = new MemoryConfiguration(this);
    }

    @Override
    public MemoryConfiguration configuration() {
        return _memoryConfiguration;
    }

    @Override
    public void addSourceChangeListener(ConfigurationSourceChangeListener listener) {
        NullArgumentChecker.DEFAULT.check(listener, "listener");
        _memoryConfiguration.addSourceChangeListener(listener);
    }
}