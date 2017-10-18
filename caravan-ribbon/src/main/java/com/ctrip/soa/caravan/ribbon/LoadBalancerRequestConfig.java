package com.ctrip.soa.caravan.ribbon;

import com.ctrip.soa.caravan.common.value.StringValues;

import java.util.Collection;
import java.util.Comparator;
import java.util.TreeSet;

/**
 * Created by w.jian on 2017/2/25.
 */
public class LoadBalancerRequestConfig {

    private TreeSet<LoadBalancerRouteConfig> routeChains;

    public LoadBalancerRequestConfig(String routeId) {
        this(new LoadBalancerRouteConfig(routeId));
    }

    public LoadBalancerRequestConfig(LoadBalancerRouteConfig loadBalancerRouteConfig) {
        this.routeChains = new TreeSet<>(RouteConfigComparator.INSTANCE);
        if (loadBalancerRouteConfig != null)
            this.routeChains.add(loadBalancerRouteConfig);
    }

    public LoadBalancerRequestConfig(Collection<LoadBalancerRouteConfig> routeChains) {
        this.routeChains = new TreeSet<>(RouteConfigComparator.INSTANCE);
        if (routeChains != null)
            this.routeChains.addAll(routeChains);
    }

    public TreeSet<LoadBalancerRouteConfig> getRouteChains() {
        return routeChains;
    }
    
    @Override
    public String toString() {
        return "{\"routeChains\":[" + StringValues.join(routeChains, ',') + "]}";
    }
    
    static class RouteConfigComparator implements Comparator<LoadBalancerRouteConfig> {

        static final RouteConfigComparator INSTANCE = new RouteConfigComparator();

        private RouteConfigComparator() {}

        @Override
        public int compare(LoadBalancerRouteConfig o1, LoadBalancerRouteConfig o2) {
            return Integer.compare(o2.getPriority(), o1.getPriority());
        }
    }
}
