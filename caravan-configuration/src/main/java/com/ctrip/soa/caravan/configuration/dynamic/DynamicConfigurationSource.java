package com.ctrip.soa.caravan.configuration.dynamic;

import com.ctrip.soa.caravan.configuration.ConfigurationSource;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public interface DynamicConfigurationSource extends ConfigurationSource {

    void addSourceChangeListener(ConfigurationSourceChangeListener listener);

}
