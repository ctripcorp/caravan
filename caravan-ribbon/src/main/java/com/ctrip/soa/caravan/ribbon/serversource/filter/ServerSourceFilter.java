package com.ctrip.soa.caravan.ribbon.serversource.filter;

import com.ctrip.soa.caravan.ribbon.LoadBalancerRoute;
import com.ctrip.soa.caravan.ribbon.LoadBalancerRequestConfig;
import com.ctrip.soa.caravan.ribbon.SeekRouteListener;

import java.util.List;

/**
 * Created by w.jian on 2016/7/26.
 */
public interface ServerSourceFilter {

    List<LoadBalancerRoute> getLoadBalancerRoutes();

    void setLoadBalancerRoutes(List<LoadBalancerRoute> loadBalancerRoutes);

    LoadBalancerRoute getLoadBalancerRoute(LoadBalancerRequestConfig loadBalancerRequestConfig);

    void refresh();

    void addServerContextChangeListener(ServerContextChangeListener listener);
    
    void addSeekRouteListener(SeekRouteListener listener);
}
