package com.ctrip.soa.caravan.common.metric;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class NullEventMetric implements EventMetric {

    public static final NullEventMetric INSTANCE = new NullEventMetric();

    private static final String NULL_METRIC_ID = "null_event_metric";

    private Map<String, String> _metadata = Collections.emptyMap();

    private Collection<String> _eventTypes = Collections.emptySet();

    private NullEventMetric() {

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
    public void addEvent(String eventType) {

    }

    @Override
    public Collection<String> getEventTypes() {
        return _eventTypes;
    }

    @Override
    public long getCount() {
        return 0;
    }

    @Override
    public long getCount(String eventType) {
        return 0;
    }

}
