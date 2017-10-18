package com.ctrip.soa.caravan.configuration.typed.corrected;

import com.ctrip.soa.caravan.configuration.corrected.CorrectedProperty;
import com.ctrip.soa.caravan.configuration.typed.TypedProperty;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public interface TypedCorrectedProperty<T> extends TypedProperty<T>, CorrectedProperty {

}