package com.ctrip.soa.caravan.configuration.dynamic;

import com.ctrip.soa.caravan.configuration.ConfigurationManager;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public interface DynamicConfigurationManager extends ConfigurationManager {
    
    DynamicProperty getProperty(String key);

    void addPropertyChangeListener(PropertyChangeListener listener);

    void addPropertyChangeListener(String key, PropertyChangeListener listener);

}
