package com.ctrip.soa.caravan.ribbon;

import com.ctrip.soa.caravan.util.serializer.JacksonJsonSerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Objects;

/**
 * Created by w.jian on 2017/3/6.
 */
@JsonPropertyOrder({"routeId", "priority", "allowFallback"})
public class LoadBalancerRouteConfig {

    private String routeId;
    private int priority;
    private boolean allowFallback;

    public LoadBalancerRouteConfig(String routeId) {
        this(routeId, 0, false);
    }

    public LoadBalancerRouteConfig(String routeId, int priority, boolean allowFallback) {
        this.routeId = routeId;
        this.priority = priority;
        this.allowFallback = allowFallback;
    }

    @JsonProperty
    public String getRouteId() {
        return routeId;
    }

    @JsonProperty
    public int getPriority() {
        return priority;
    }

    @JsonProperty
    public boolean isAllowFallback() {
        return allowFallback;
    }

    @Override
    public String toString() {
        return JacksonJsonSerializer.INSTANCE.serialize(this);
    }

    @Override
    public int hashCode() {
        if (getRouteId() == null)
            return 0;
        return getRouteId().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;

        if (!(obj instanceof LoadBalancerRouteConfig))
            return false;
        return Objects.equals(getRouteId(), ((LoadBalancerRouteConfig)obj).getRouteId());
    }
}
