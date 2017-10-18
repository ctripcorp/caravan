package com.ctrip.soa.caravan.ribbon;

import com.ctrip.soa.caravan.common.delegate.Func;

public interface LoadBalancerRuleFactoryManager {
    
    LoadBalancerRule getLoadBalancerRule(String ruleId);
    
    Func<LoadBalancerRule> getLoadBalancerRuleFactory();
    
    void registerLoadBalancerRuleFactory(Func<LoadBalancerRule> rule);
}
