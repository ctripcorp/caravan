package com.ctrip.soa.caravan.configuration;

import com.ctrip.soa.caravan.common.value.checker.NullArgumentChecker;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class DefaultProperty implements Property {

    private ConfigurationManager _manager;
    private String _key;

    public DefaultProperty(ConfigurationManager manager, String key) {
        NullArgumentChecker.DEFAULT.check(manager, "manager");
        NullArgumentChecker.DEFAULT.check(key, "key");

        _manager = manager;
        _key = key;
    }

    protected ConfigurationManager manager() {
        return _manager;
    }

    @Override
    public String key() {
        return _key;
    }

    @Override
    public String value() {
        return manager().getPropertyValue(key());
    }

    @Override
    public String toString() {
        return String.format("{ key: %s, value: %s }", key(), value());
    }

}