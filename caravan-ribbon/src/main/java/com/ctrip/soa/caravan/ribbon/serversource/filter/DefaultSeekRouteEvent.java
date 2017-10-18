package com.ctrip.soa.caravan.ribbon.serversource.filter;

import com.ctrip.soa.caravan.ribbon.LoadBalancerRoute;
import com.ctrip.soa.caravan.ribbon.SeekRouteEvent;

/**
 * Created by w.jian on 2017/6/7.
 */
class DefaultSeekRouteEvent implements SeekRouteEvent {
    
    private LoadBalancerRoute route;
    
    public DefaultSeekRouteEvent(LoadBalancerRoute route) {
        this.route = route;
    }
    
    @Override
    public LoadBalancerRoute getRoute() {
        return route;
    }
    
    @Override
    public void setRoute(LoadBalancerRoute route) {
        this.route = route;
    }
}
