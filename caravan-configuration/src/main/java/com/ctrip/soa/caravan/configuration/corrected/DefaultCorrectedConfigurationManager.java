package com.ctrip.soa.caravan.configuration.corrected;

import com.ctrip.soa.caravan.common.value.corrector.ValueCorrector;
import com.ctrip.soa.caravan.configuration.ConfigurationManager;
import com.ctrip.soa.caravan.configuration.Property;
import com.ctrip.soa.caravan.configuration.wrapper.ConfigurationManagerWrapper;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class DefaultCorrectedConfigurationManager extends ConfigurationManagerWrapper implements CorrectedConfigurationManager {

    public DefaultCorrectedConfigurationManager(ConfigurationManager manager) {
        super(manager);
    }

    @Override
    public CorrectedProperty getProperty(String key, ValueCorrector<String> valueCorrector) {
        Property property = getProperty(key);
        return new DefaultCorrectedProperty(property, valueCorrector);
    }

}
