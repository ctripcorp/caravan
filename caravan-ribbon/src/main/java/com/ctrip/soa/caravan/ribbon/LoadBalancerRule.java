package com.ctrip.soa.caravan.ribbon;

/**
 * Created by w.jian on 2016/6/14.
 */
public interface LoadBalancerRule {

    String getRuleId();

    String getDescription();

    Server choose(LoadBalancerRoute loadBalancerRoute);
}
