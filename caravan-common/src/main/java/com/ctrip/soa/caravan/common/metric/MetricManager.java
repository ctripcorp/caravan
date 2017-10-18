package com.ctrip.soa.caravan.common.metric;

import java.util.Collection;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public interface MetricManager<T extends Metric> {

    String managerId();

    MetricManagerConfig<T> config();

    Collection<T> metrics();

}
