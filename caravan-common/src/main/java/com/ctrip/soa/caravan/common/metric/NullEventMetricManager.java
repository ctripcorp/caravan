package com.ctrip.soa.caravan.common.metric;

import java.util.Collection;
import java.util.Collections;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class NullEventMetricManager implements EventMetricManager {

    public static final NullEventMetricManager INSTANCE = new NullEventMetricManager();

    private static final String NULL_MANAGER_ID = "null_event_metric_manager";

    private NullEventMetricManager() {

    }

    @Override
    public String managerId() {
        return NULL_MANAGER_ID;
    }

    @Override
    public MetricManagerConfig<EventMetric> config() {
        return MetricManagerConfig.NULL_EVENT_METRIC_MANAGER_CONFIG;
    }

    @Override
    public EventMetric getMetric(String metricId, MetricConfig metricConfig) {
        return NullEventMetric.INSTANCE;
    }

    @Override
    public Collection<EventMetric> metrics() {
        return Collections.emptyList();
    }

}
