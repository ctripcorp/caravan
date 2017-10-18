package com.ctrip.soa.caravan.common.metric;

/**
 * Created by w.jian on 2016/9/12.
 */
public interface StatusProvider<T> {
    T getStatus();
}
