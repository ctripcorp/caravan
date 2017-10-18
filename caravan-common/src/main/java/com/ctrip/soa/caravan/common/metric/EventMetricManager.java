package com.ctrip.soa.caravan.common.metric;

/**
 * Created by w.jian on 2016/9/12.
 */
public interface EventMetricManager extends MetricManager<EventMetric> {
    EventMetric getMetric(String metricId, MetricConfig metricConfig);
}
