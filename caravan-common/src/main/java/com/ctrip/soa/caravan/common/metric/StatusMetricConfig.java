package com.ctrip.soa.caravan.common.metric;

import com.ctrip.soa.caravan.common.value.checker.NullArgumentChecker;

import java.util.Map;

/**
 * Created by w.jian on 2016/9/12.
 */
public class StatusMetricConfig<T> extends MetricConfig {

    private StatusProvider<T> _statusProvider;

    public StatusMetricConfig(StatusProvider<T> statusProvider, Map<String, String> metadata) {
        super(metadata);

        NullArgumentChecker.DEFAULT.check(statusProvider, "statusProvider");
        _statusProvider = statusProvider;
    }

    public StatusProvider<T> statusProvider() {
        return _statusProvider;
    }
}
