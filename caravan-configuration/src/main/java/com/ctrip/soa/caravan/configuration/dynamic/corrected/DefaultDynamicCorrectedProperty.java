package com.ctrip.soa.caravan.configuration.dynamic.corrected;

import com.ctrip.soa.caravan.configuration.corrected.CorrectedProperty;
import com.ctrip.soa.caravan.configuration.dynamic.DefaultDynamicProperty;
import com.ctrip.soa.caravan.configuration.dynamic.DynamicConfigurationManager;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class DefaultDynamicCorrectedProperty extends DefaultDynamicProperty implements DynamicCorrectedProperty {

    public DefaultDynamicCorrectedProperty(DynamicConfigurationManager manager, CorrectedProperty property) {
        super(manager, property);
    }

}
