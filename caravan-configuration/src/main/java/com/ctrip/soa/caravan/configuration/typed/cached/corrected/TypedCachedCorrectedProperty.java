package com.ctrip.soa.caravan.configuration.typed.cached.corrected;

import com.ctrip.soa.caravan.configuration.typed.cached.TypedCachedProperty;
import com.ctrip.soa.caravan.configuration.typed.corrected.TypedCorrectedProperty;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public interface TypedCachedCorrectedProperty<T> extends TypedCachedProperty<T>, TypedCorrectedProperty<T> {

}
