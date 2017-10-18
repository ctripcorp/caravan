package com.ctrip.soa.caravan.configuration.typed.cached;

import com.ctrip.soa.caravan.configuration.cached.CachedProperty;
import com.ctrip.soa.caravan.configuration.typed.TypedProperty;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public interface TypedCachedProperty<T> extends TypedProperty<T>, CachedProperty {

}
