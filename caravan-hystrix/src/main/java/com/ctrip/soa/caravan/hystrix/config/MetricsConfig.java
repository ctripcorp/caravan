package com.ctrip.soa.caravan.hystrix.config;

import com.ctrip.soa.caravan.common.concurrent.collect.circularbuffer.timebucket.PercentileBufferConfig;
import com.ctrip.soa.caravan.common.concurrent.collect.circularbuffer.timebucket.TimeBufferConfig;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public interface MetricsConfig {

    String HEALTH_SNAPSHOT_INTERVAL_PROPERTY_KEY = "execution.metrics.health-snapshot-interval";

    TimeBufferConfig eventCounterConfig();

    long healthSnapshotInterval();

    PercentileBufferConfig executionLatencyConfig();

}
