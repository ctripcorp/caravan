package com.ctrip.soa.caravan.configuration.cached;

import com.ctrip.soa.caravan.configuration.ConfigurationManager;
import com.ctrip.soa.caravan.configuration.wrapper.ConfigurationManagerWrapper;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class DefaultCachedConfigurationManager extends ConfigurationManagerWrapper implements CachedConfigurationManager {

    public DefaultCachedConfigurationManager(ConfigurationManager manager) {
        super(manager);
    }

    @Override
    public CachedProperty getProperty(String key) {
        return new DefaultCachedProperty(super.getProperty(key));
    }
}