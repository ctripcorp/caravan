package com.ctrip.soa.caravan.configuration.dynamic.cached.corrected;

import com.ctrip.soa.caravan.common.value.corrector.ValueCorrector;
import com.ctrip.soa.caravan.configuration.cached.corrected.CachedCorrectedConfigurationManager;
import com.ctrip.soa.caravan.configuration.dynamic.cached.DynamicCachedConfigurationManager;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public interface DynamicCachedCorrectedConfigurationManager
        extends DynamicCachedConfigurationManager, CachedCorrectedConfigurationManager {

    DynamicCachedCorrectedProperty getProperty(String key, ValueCorrector<String> valueCorrector);

}
