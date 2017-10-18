package com.ctrip.soa.caravan.configuration.dynamic.cached;

import com.ctrip.soa.caravan.configuration.ConfigurationManager;
import com.ctrip.soa.caravan.configuration.dynamic.DefaultDynamicConfigurationManager;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class DefaultDynamicCachedConfigurationManager extends DefaultDynamicConfigurationManager
        implements DynamicCachedConfigurationManager {

    public DefaultDynamicCachedConfigurationManager(ConfigurationManager manager) {
        super(manager);
    }

    @Override
    public DynamicCachedProperty getProperty(String key) {
        return new DefaultDynamicCachedProperty(super.getProperty(key));
    }

}
