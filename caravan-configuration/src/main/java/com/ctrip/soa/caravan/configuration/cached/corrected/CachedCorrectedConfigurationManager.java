package com.ctrip.soa.caravan.configuration.cached.corrected;

import com.ctrip.soa.caravan.common.value.corrector.ValueCorrector;
import com.ctrip.soa.caravan.configuration.cached.CachedConfigurationManager;
import com.ctrip.soa.caravan.configuration.corrected.CorrectedConfigurationManager;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public interface CachedCorrectedConfigurationManager extends CachedConfigurationManager, CorrectedConfigurationManager {

    CachedCorrectedProperty getProperty(String key, ValueCorrector<String> valueCorrector);

}
