package com.ctrip.soa.caravan.configuration.dynamic.corrected;

import com.ctrip.soa.caravan.common.value.corrector.ValueCorrector;
import com.ctrip.soa.caravan.configuration.corrected.CorrectedConfigurationManager;
import com.ctrip.soa.caravan.configuration.dynamic.DefaultDynamicConfigurationManager;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class DefaultDynamicCorrectedConfigurationManager extends DefaultDynamicConfigurationManager implements DynamicCorrectedConfigurationManager {

    public DefaultDynamicCorrectedConfigurationManager(CorrectedConfigurationManager manager) {
        super(manager);
    }
    
    @Override
    protected CorrectedConfigurationManager manager() {
        return (CorrectedConfigurationManager)super.manager();
    }

    @Override
    public DynamicCorrectedProperty getProperty(String key, ValueCorrector<String> valueCorrector) {
        return new DefaultDynamicCorrectedProperty(this, manager().getProperty(key, valueCorrector));
    }

}
