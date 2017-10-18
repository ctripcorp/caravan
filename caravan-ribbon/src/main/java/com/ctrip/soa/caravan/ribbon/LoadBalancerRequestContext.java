package com.ctrip.soa.caravan.ribbon;

/**
 * Created by w.jian on 2016/7/13.
 */
public interface LoadBalancerRequestContext {

    Server getServer();

    void markServerAvailable();

    void markServerUnavailable();
}
