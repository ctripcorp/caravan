package com.ctrip.soa.caravan.common.delegate;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public interface Func2<T1, T2, V> {

    V execute(T1 param1, T2 param2);

}
