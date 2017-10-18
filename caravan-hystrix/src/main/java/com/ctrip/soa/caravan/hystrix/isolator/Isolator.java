package com.ctrip.soa.caravan.hystrix.isolator;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public interface Isolator {

    boolean allowExecution();

    void markComplete();

    long concurrentCount();

}
