package com.ctrip.soa.caravan.configuration.cached.corrected;

import com.ctrip.soa.caravan.common.value.corrector.ValueCorrector;
import com.ctrip.soa.caravan.configuration.cached.DefaultCachedConfigurationManager;
import com.ctrip.soa.caravan.configuration.corrected.CorrectedConfigurationManager;
import com.ctrip.soa.caravan.configuration.corrected.CorrectedProperty;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class DefaultCachedCorrectedConfigurationManager extends DefaultCachedConfigurationManager implements CachedCorrectedConfigurationManager {
    
    public DefaultCachedCorrectedConfigurationManager(CorrectedConfigurationManager manager) {
        super(manager);
    }
    
    @Override
    public CorrectedConfigurationManager manager() {
        return (CorrectedConfigurationManager)super.manager();
    }
    
    public CachedCorrectedProperty getProperty(String key, ValueCorrector<String> valueCorrector) {
        CorrectedProperty property = manager().getProperty(key, valueCorrector);
        return new DefaultCachedCorrectedProperty(property);
    }

}
