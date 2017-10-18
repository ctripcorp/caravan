package com.ctrip.soa.caravan.configuration.dynamic.cached;

import com.ctrip.soa.caravan.configuration.cached.CachedProperty;
import com.ctrip.soa.caravan.configuration.dynamic.PropertyChangeEvent;
import com.ctrip.soa.caravan.configuration.dynamic.WeakReferencePropertyChangeListener;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class CachedPropertyWeakReferencePropertyChangeListener extends WeakReferencePropertyChangeListener<CachedProperty> {

    public CachedPropertyWeakReferencePropertyChangeListener(CachedProperty data) {
        super(data);
    }

    @Override
    public void onChange(PropertyChangeEvent e) {
        CachedProperty cachedProperty = data();
        if (cachedProperty == null)
            return;

        cachedProperty.refresh();
    }

}
