package com.ctrip.soa.caravan.configuration.dynamic.cached;

import com.ctrip.soa.caravan.configuration.cached.CachedConfigurationManager;
import com.ctrip.soa.caravan.configuration.dynamic.DynamicConfigurationManager;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public interface DynamicCachedConfigurationManager extends DynamicConfigurationManager, CachedConfigurationManager {
    
    DynamicCachedProperty getProperty(String key);

}
