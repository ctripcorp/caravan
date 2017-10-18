package com.ctrip.soa.caravan.configuration.wrapper;

import java.util.Collection;

import com.ctrip.soa.caravan.common.value.checker.NullArgumentChecker;
import com.ctrip.soa.caravan.configuration.ConfigurationManager;
import com.ctrip.soa.caravan.configuration.ConfigurationSource;
import com.ctrip.soa.caravan.configuration.Property;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class ConfigurationManagerWrapper implements ConfigurationManager {

    private ConfigurationManager _manager;

    protected ConfigurationManager manager() {
        return _manager;
    }

    public ConfigurationManagerWrapper(ConfigurationManager manager) {
        NullArgumentChecker.DEFAULT.check(manager, "manager");
        _manager = manager;
    }

    @Override
    public String getPropertyValue(String key) {
        return manager().getPropertyValue(key);
    }

    @Override
    public Property getProperty(String key) {
        return manager().getProperty(key);
    }

    @Override
    public Collection<ConfigurationSource> sources() {
        return manager().sources();
    }

}
