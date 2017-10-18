package com.ctrip.soa.caravan.common.delegate;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public interface Action2<T1, T2> {

    void execute(T1 param1, T2 param2);

}
