package com.ctrip.soa.caravan.ribbon.serversource.manager;

import com.ctrip.soa.caravan.ribbon.LoadBalancerRoute;

import java.util.List;

/**
 * Created by w.jian on 2017/4/27.
 */
public class ServerSourceRestoreEvent {
    
    private List<LoadBalancerRoute> routes;
    
    ServerSourceRestoreEvent(List<LoadBalancerRoute> routes) {
        this.routes = routes;
    }
    
    public List<LoadBalancerRoute> getRoutes() {
        return routes;
    }
    
    public void setRoutes(List<LoadBalancerRoute> routes) {
        this.routes = routes;
    }
}
