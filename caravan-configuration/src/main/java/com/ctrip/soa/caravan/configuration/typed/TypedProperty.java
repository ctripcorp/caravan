package com.ctrip.soa.caravan.configuration.typed;

import com.ctrip.soa.caravan.configuration.Property;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public interface TypedProperty<T> extends Property {

    T typedValue();

}
