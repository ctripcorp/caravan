package com.ctrip.soa.caravan.ribbon;

import com.ctrip.soa.caravan.common.metric.*;
import com.ctrip.soa.caravan.common.value.checker.NullArgumentChecker;
import com.ctrip.soa.caravan.configuration.facade.TypedDynamicCachedCorrectedProperties;

/**
 * Created by w.jian on 2016/9/1.
 */
public class LoadBalancerManagerConfig {

    private TypedDynamicCachedCorrectedProperties _properties;

    private EventMetricManager _eventMetricManager;

    private AuditMetricManager _auditMetricManager;

    private StatusMetricManager<Double> _statusMetricManager;

    public LoadBalancerManagerConfig(TypedDynamicCachedCorrectedProperties properties) {
        this(properties, null, null, null);
    }

    public LoadBalancerManagerConfig(TypedDynamicCachedCorrectedProperties properties,
                                     EventMetricManager eventMetricManager,
                                     AuditMetricManager valueMetricManager,
                                     StatusMetricManager<Double> statusMetricManager) {
        NullArgumentChecker.DEFAULT.check(properties, "properties");
        _properties = properties;

        if (eventMetricManager == null)
            eventMetricManager = NullEventMetricManager.INSTANCE;
        if (valueMetricManager == null)
            valueMetricManager = NullAuditMetricManager.INSTANCE;
        if (statusMetricManager == null)
            statusMetricManager = NullStatusMetricManager.DOUBLE_NULL_STATUS_METRIC_MANAGER;

        _eventMetricManager = eventMetricManager;
        _auditMetricManager = valueMetricManager;
        _statusMetricManager = statusMetricManager;
    }

    public TypedDynamicCachedCorrectedProperties getProperties() {
        return _properties;
    }

    public AuditMetricManager getValueMetricManager() {
        return _auditMetricManager;
    }

    public EventMetricManager getEventMetricManager() {
        return _eventMetricManager;
    }

    public StatusMetricManager<Double> getStatusMetricManager() {
        return _statusMetricManager;
    }
}
