package com.ctrip.soa.caravan.common.value.corrector;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public interface ValueCorrector<T> {
    
    T correct(T value);

}
