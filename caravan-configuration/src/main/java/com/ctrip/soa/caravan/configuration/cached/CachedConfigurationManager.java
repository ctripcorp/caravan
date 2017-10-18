package com.ctrip.soa.caravan.configuration.cached;

import com.ctrip.soa.caravan.configuration.ConfigurationManager;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public interface CachedConfigurationManager extends ConfigurationManager {

    CachedProperty getProperty(String key);

}