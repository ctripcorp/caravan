package com.ctrip.soa.caravan.ribbon;

/**
 * Created by w.jian on 2017/2/25.
 */
public class ConfigurationKeys {

    public static final String DataFolder = "ribbon.local-cache.data-folder";

    public static final String CheckInterval = "ribbon.check-interval-in-millisecond";

    public static final String FailureThresholdPercentage = "ribbon.failure-threshold-percentage";

    public static final String CounterBuffer = "ribbon.counter-buffer";

    public static final String LoadBalancerRuleName = "ribbon.lb.rule.name";
    
    public static final String MinAvailableServerCount = "ribbon.min-available-server-count";
    
    public static final String CheckHealthCount = "ribbon.checkhealth.thread-count";
}
