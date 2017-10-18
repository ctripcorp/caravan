package com.ctrip.soa.caravan.common.metric;

import java.util.Collection;
import java.util.Collections;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class NullAuditMetricManager implements AuditMetricManager {

    public static final NullAuditMetricManager INSTANCE = new NullAuditMetricManager();

    private static final String NULL_MANAGER_ID = "null_audit_metric_manager";

    private NullAuditMetricManager() {

    }

    @Override
    public String managerId() {
        return NULL_MANAGER_ID;
    }

    @Override
    public MetricManagerConfig<AuditMetric> config() {
        return MetricManagerConfig.NULL_VALUE_METRIC_MANAGER_CONFIG;
    }

    @Override
    public AuditMetric getMetric(String metricId, MetricConfig metricConfig) {
        return NullAuditMetric.INSTANCE;
    }

    @Override
    public Collection<AuditMetric> metrics() {
        return Collections.emptyList();
    }

}
