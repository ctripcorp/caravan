package com.ctrip.soa.caravan.ribbon;

import java.util.List;

/**
 * Created by w.jian on 2016/6/15.
 */
public interface ServerSource {

    List<LoadBalancerRoute> getLoadBalancerRoutes();
}
