package com.ctrip.soa.caravan.common.metric;

import java.util.Collections;
import java.util.Map;

/**
 * Created by w.jian on 2016/9/12.
 */
public class NullStatusMetric<T> implements StatusMetric<T> {

    private static String metricId = "null_status_metric";
    private T _status;
    private Map<String, String> _metadata = Collections.emptyMap();

    NullStatusMetric(T status) {
        _status = status;
    }

    @Override
    public T getStatus() {
        return _status;
    }

    @Override
    public String metricId() {
        return metricId;
    }

    @Override
    public Map<String, String> metadata() {
        return _metadata;
    }
}
