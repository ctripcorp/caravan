package com.ctrip.soa.caravan.configuration.defaultvalue;

import com.ctrip.soa.caravan.configuration.ConfigurationManager;
import com.ctrip.soa.caravan.configuration.Property;
import com.ctrip.soa.caravan.configuration.wrapper.ConfigurationManagerWrapper;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class DefaultValueConfigurationManager extends ConfigurationManagerWrapper {

    public DefaultValueConfigurationManager(ConfigurationManager manager) {
        super(manager);
    }

    public String getPropertyValue(String key, String defaultValue) {
        String value = super.getPropertyValue(key);
        if (value == null)
            return defaultValue;
        return value;
    }

    public Property getProperty(String key, String defaultValue) {
        return new DefaultValueWrapper(super.getProperty(key), defaultValue);
    }
}
