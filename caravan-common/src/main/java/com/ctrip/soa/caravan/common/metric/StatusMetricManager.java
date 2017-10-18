package com.ctrip.soa.caravan.common.metric;

/**
 * Created by w.jian on 2016/9/12.
 */
public interface StatusMetricManager<T> extends MetricManager<StatusMetric<T>> {

    StatusMetric<T> getMetric(String metricId, StatusMetricConfig<T> metricConfig);
}
