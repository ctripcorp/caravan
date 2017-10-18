package com.ctrip.soa.caravan.common.metric;

import com.ctrip.soa.caravan.common.delegate.Func;
import com.ctrip.soa.caravan.common.value.ConcurrentHashMapValues;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by w.jian on 2016/9/12.
 */
public class NullStatusMetricManager<T> implements StatusMetricManager<T> {

    private static ConcurrentHashMap<Class<?>, NullStatusMetricManager<?>> _managerCache = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public static <T> NullStatusMetricManager<T> getInstance(Class<T> statusClass, final T status) {
        return (NullStatusMetricManager<T>) ConcurrentHashMapValues.getOrAddWithLock(_managerCache, statusClass, new Func<NullStatusMetricManager<?>>() {

            @Override
            public NullStatusMetricManager<?> execute() {
                return new NullStatusMetricManager<T>(status);
            }
        });
    }

    public static final NullStatusMetricManager<Double> DOUBLE_NULL_STATUS_METRIC_MANAGER = NullStatusMetricManager.getInstance(Double.class, 0.0);

    private static final String metricIdPrefix = "null_status_manager_";

    private String _metricId;
    private NullStatusMetric<T> _metric;
    private MetricManagerConfig<StatusMetric<T>> _managerConfig = new MetricManagerConfig<>(NullStatusMetricReporter.<T> getInstance());

    private NullStatusMetricManager(T status) {
        _metricId = metricIdPrefix + status.getClass().getName();
        _metric = new NullStatusMetric<>(status);
    }

    @Override
    public StatusMetric<T> getMetric(String metricId, StatusMetricConfig<T> metricConfig) {
        return _metric;
    }

    @Override
    public String managerId() {
        return _metricId;
    }

    @Override
    public MetricManagerConfig<StatusMetric<T>> config() {
        return _managerConfig;
    }

    @Override
    public Collection<StatusMetric<T>> metrics() {
        return Collections.emptyList();
    }
}
