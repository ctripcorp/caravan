package com.ctrip.soa.caravan.ribbon.serversource.manager;

import com.ctrip.soa.caravan.ribbon.LoadBalancerRoute;

import java.util.List;

/**
 * Created by w.jian on 2016/7/18.
 */
public interface ServerSourceManager {

    void backup(List<LoadBalancerRoute> routes);

    List<LoadBalancerRoute> restore();
    
    void addRestoreListener(ServerSourceRestoreListener listener);
}
