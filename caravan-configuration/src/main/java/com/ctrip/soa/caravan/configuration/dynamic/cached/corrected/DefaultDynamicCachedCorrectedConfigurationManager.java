package com.ctrip.soa.caravan.configuration.dynamic.cached.corrected;

import com.ctrip.soa.caravan.common.value.corrector.ValueCorrector;
import com.ctrip.soa.caravan.configuration.corrected.CorrectedConfigurationManager;
import com.ctrip.soa.caravan.configuration.corrected.CorrectedProperty;
import com.ctrip.soa.caravan.configuration.dynamic.cached.DefaultDynamicCachedConfigurationManager;
import com.ctrip.soa.caravan.configuration.dynamic.corrected.DefaultDynamicCorrectedProperty;
import com.ctrip.soa.caravan.configuration.dynamic.corrected.DynamicCorrectedProperty;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class DefaultDynamicCachedCorrectedConfigurationManager extends DefaultDynamicCachedConfigurationManager implements DynamicCachedCorrectedConfigurationManager {

    public DefaultDynamicCachedCorrectedConfigurationManager(CorrectedConfigurationManager manager) {
        super(manager);
    }
    
    @Override
    protected CorrectedConfigurationManager manager() {
        return (CorrectedConfigurationManager)super.manager();
    }
    
    @Override
    public DynamicCachedCorrectedProperty getProperty(String key, ValueCorrector<String> valueCorrector) {
        CorrectedProperty correctedProperty = manager().getProperty(key, valueCorrector);
        DynamicCorrectedProperty dynamicCorrectedProperty = new DefaultDynamicCorrectedProperty(this, correctedProperty);
        return new DefaultDynamicCachedCorrectedProperty(dynamicCorrectedProperty);
    }
    
}
