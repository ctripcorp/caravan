package com.ctrip.soa.caravan.common.metric;

import com.ctrip.soa.caravan.common.collect.AuditData;

import java.util.Collections;
import java.util.Map;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class NullAuditMetric implements AuditMetric {

    public static final NullAuditMetric INSTANCE = new NullAuditMetric();

    private static final String NULL_METRIC_ID = "null_audit_metric";

    private Map<String, String> _metadata = Collections.emptyMap();

    private AuditData _auditData = new AuditData() {

        @Override
        public void setMax(long max) {
            throw new IllegalStateException("AuditData of NullAuditMetric cannot be modified.");
        }

        @Override
        public void setMin(long min) {
            throw new IllegalStateException("AuditData of NullAuditMetric cannot be modified.");
        }

        @Override
        public void setSum(long sum) {
            throw new IllegalStateException("AuditData of NullAuditMetric cannot be modified.");
        }

        @Override
        public void setCount(int count) {
            throw new IllegalStateException("AuditData of NullAuditMetric cannot be modified.");
        }

    };

    private NullAuditMetric() {

    }

    @Override
    public String metricId() {
        return NULL_METRIC_ID;
    }

    @Override
    public Map<String, String> metadata() {
        return _metadata;
    }

    @Override
    public void addValue(double value) {

    }

    @Override
    public long getCount() {
        return 0;
    }

    @Override
    public long getCountInRange(long lowerBound, long upperBound) {
        return 0;
    }

    @Override
    public long getPercentile(double percent) {
        return 0;
    }

    @Override
    public AuditData getAuditData() {
        return _auditData;
    }

}
