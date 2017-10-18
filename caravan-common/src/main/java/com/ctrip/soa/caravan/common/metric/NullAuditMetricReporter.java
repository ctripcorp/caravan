package com.ctrip.soa.caravan.common.metric;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class NullAuditMetricReporter implements MetricReporter<AuditMetric> {

    public static final NullAuditMetricReporter INSTANCE = new NullAuditMetricReporter();

    private NullAuditMetricReporter() {

    }

    @Override
    public void report(AuditMetric metric) {

    }

}
