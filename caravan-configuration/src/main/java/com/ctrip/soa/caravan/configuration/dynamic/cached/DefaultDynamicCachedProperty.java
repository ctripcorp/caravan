package com.ctrip.soa.caravan.configuration.dynamic.cached;

import com.ctrip.soa.caravan.configuration.cached.DefaultCachedProperty;
import com.ctrip.soa.caravan.configuration.dynamic.DynamicProperty;
import com.ctrip.soa.caravan.configuration.dynamic.PropertyChangeListener;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class DefaultDynamicCachedProperty extends DefaultCachedProperty implements DynamicCachedProperty {

    public DefaultDynamicCachedProperty(DynamicProperty property) {
        super(property);

        property().addChangeListener(new CachedPropertyWeakReferencePropertyChangeListener(this));
    }

    @Override
    protected DynamicProperty property() {
        return (DynamicProperty) super.property();
    }

    @Override
    public void addChangeListener(PropertyChangeListener listener) {
        property().addChangeListener(listener);
    }

}
