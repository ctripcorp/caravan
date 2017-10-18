package com.ctrip.soa.caravan.configuration.corrected;

import com.ctrip.soa.caravan.common.value.corrector.ValueCorrector;
import com.ctrip.soa.caravan.configuration.ConfigurationManager;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public interface CorrectedConfigurationManager extends ConfigurationManager {

    CorrectedProperty getProperty(String key, ValueCorrector<String> valueCorrector);

}
