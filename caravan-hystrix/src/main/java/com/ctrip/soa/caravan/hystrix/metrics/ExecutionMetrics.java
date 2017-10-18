package com.ctrip.soa.caravan.hystrix.metrics;

import com.ctrip.soa.caravan.common.collect.AuditData;
import com.ctrip.soa.caravan.hystrix.ExecutionEvent;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public interface ExecutionMetrics {

    void markEvent(ExecutionEvent event);

    long getEventCount(ExecutionEvent event);

    HealthSnapshot getHealthSnapshot();

    void markExecutionLatency(long latency);

    long getLatencyPercentile(double pencent);

    AuditData getLatencyAuditData();

    long getLatencyCountInRange(long low, long high);

    void reset();

}
