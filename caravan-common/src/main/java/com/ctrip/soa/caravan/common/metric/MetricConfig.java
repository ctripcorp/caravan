package com.ctrip.soa.caravan.common.metric;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by w.jian on 2016/9/12.
 */
public class MetricConfig {
    private Map<String, String> _metadata;

    public MetricConfig(Map<String, String> metadata) {
        if (metadata == null)
            metadata = new HashMap<>();
        _metadata = Collections.unmodifiableMap(metadata);
    }

    public Map<String, String> metadata() {
        return _metadata;
    }
}
