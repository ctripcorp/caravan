package com.ctrip.soa.caravan.configuration.cached.corrected;

import com.ctrip.soa.caravan.configuration.cached.DefaultCachedProperty;
import com.ctrip.soa.caravan.configuration.corrected.CorrectedProperty;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class DefaultCachedCorrectedProperty extends DefaultCachedProperty implements CachedCorrectedProperty {

    public DefaultCachedCorrectedProperty(CorrectedProperty property) {
        super(property);
    }
    
}
