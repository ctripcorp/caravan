package com.ctrip.soa.caravan.configuration;

import com.ctrip.soa.caravan.common.value.checker.StringArgumentChecker;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public abstract class AbstractConfigurationSource implements ConfigurationSource {

    private int _priority;
    private String _sourceId;

    public AbstractConfigurationSource(int priority, String sourceId) {
        StringArgumentChecker.DEFAULT.check(sourceId, "sourceId");

        _priority = priority;
        _sourceId = sourceId;
    }

    @Override
    public int priority() {
        return _priority;
    }

    @Override
    public String sourceId() {
        return _sourceId;
    }

    @Override
    public abstract Configuration configuration();

}
