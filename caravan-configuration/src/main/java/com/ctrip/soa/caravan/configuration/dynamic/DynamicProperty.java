package com.ctrip.soa.caravan.configuration.dynamic;

import com.ctrip.soa.caravan.configuration.Property;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public interface DynamicProperty extends Property {
    
    void addChangeListener(PropertyChangeListener listener);
    
}
