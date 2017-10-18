package com.ctrip.soa.caravan.common.metric;

import com.ctrip.soa.caravan.common.value.checker.NullArgumentChecker;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class MetricManagerConfig<T extends Metric> {

    public static final MetricManagerConfig<EventMetric> NULL_EVENT_METRIC_MANAGER_CONFIG = new MetricManagerConfig<>(NullEventMetricReporter.INSTANCE);
    public static final MetricManagerConfig<AuditMetric> NULL_VALUE_METRIC_MANAGER_CONFIG = new MetricManagerConfig<>(NullAuditMetricReporter.INSTANCE);

    private MetricReporter<T> _metricReporter;

    public MetricManagerConfig(MetricReporter<T> metricReporter) {
        NullArgumentChecker.DEFAULT.check(metricReporter, "metricReporter");
        _metricReporter = metricReporter;
    }

    public MetricReporter<T> metricReporter() {
        return _metricReporter;
    }

}
