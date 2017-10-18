package com.ctrip.soa.caravan.hystrix.metrics;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public interface HealthSnapshot {

    long totalCount();

    int errorPercentage();

}
