package com.ctrip.soa.caravan.configuration.dynamic.cached.corrected;

import com.ctrip.soa.caravan.configuration.dynamic.cached.DefaultDynamicCachedProperty;
import com.ctrip.soa.caravan.configuration.dynamic.corrected.DynamicCorrectedProperty;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class DefaultDynamicCachedCorrectedProperty extends DefaultDynamicCachedProperty implements DynamicCachedCorrectedProperty {

    public DefaultDynamicCachedCorrectedProperty(DynamicCorrectedProperty property) {
        super(property);
    }

}
