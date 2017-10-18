package com.ctrip.soa.caravan.common.value.converter;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public interface ValueConverter<S, D> {

    D convert(S source);

}
