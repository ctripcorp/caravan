package com.ctrip.soa.caravan.configuration.cached;

import com.ctrip.soa.caravan.configuration.Property;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public interface CachedProperty extends Property {

    void refresh();

}