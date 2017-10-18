package com.ctrip.soa.caravan.common.metric;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class NullEventMetricReporter implements MetricReporter<EventMetric> {

    public static final NullEventMetricReporter INSTANCE = new NullEventMetricReporter();

    private NullEventMetricReporter() {

    }

    @Override
    public void report(EventMetric metric) {

    }

}
