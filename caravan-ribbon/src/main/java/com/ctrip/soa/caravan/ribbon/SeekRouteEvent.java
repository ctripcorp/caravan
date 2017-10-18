package com.ctrip.soa.caravan.ribbon;

/**
 * Created by w.jian on 2017/6/7.
 */
public interface SeekRouteEvent {
    
    LoadBalancerRoute getRoute();
    
    void setRoute(LoadBalancerRoute route);
}
