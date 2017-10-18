package com.ctrip.soa.caravan.configuration.dynamic.corrected;

import com.ctrip.soa.caravan.common.value.corrector.ValueCorrector;
import com.ctrip.soa.caravan.configuration.corrected.CorrectedConfigurationManager;
import com.ctrip.soa.caravan.configuration.dynamic.DynamicConfigurationManager;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public interface DynamicCorrectedConfigurationManager
        extends DynamicConfigurationManager, CorrectedConfigurationManager {

    DynamicCorrectedProperty getProperty(String key, ValueCorrector<String> valueCorrector);

}
