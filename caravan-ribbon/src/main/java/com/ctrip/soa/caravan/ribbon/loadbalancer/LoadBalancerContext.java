package com.ctrip.soa.caravan.ribbon.loadbalancer;

import com.ctrip.soa.caravan.common.metric.*;
import com.ctrip.soa.caravan.configuration.facade.TypedDynamicCachedCorrectedProperties;
import com.ctrip.soa.caravan.ribbon.*;
import com.ctrip.soa.caravan.ribbon.server.ServerStats;
import com.ctrip.soa.caravan.ribbon.serversource.filter.ServerSourceFilter;
import com.ctrip.soa.caravan.ribbon.serversource.manager.ServerSourceManager;
import com.ctrip.soa.caravan.ribbon.serversource.monitor.ServerSourceMonitor;

import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by w.jian on 2016/7/19.
 */
public interface LoadBalancerContext {

    String managerId();

    String loadBalancerId();

    String loadBalancerKey();

    Ping ping();

    LoadBalancerRule getLoadBalancerRule(String ruleId);

    LoadBalancer loadBalancer();

    LoadBalancerConfig loadBalancerConfig();

    ServerSource serverSource();

    ServerSourceFilter serverSourceFilter();

    ServerSourceManager serverSourceManager();

    ServerSourceMonitor serverSourceMonitor();
    
    TypedDynamicCachedCorrectedProperties properties();

    AuditMetricManager auditMetricManager();

    EventMetricManager eventMetricManager();

    StatusMetricManager<Double> statusMetricManager();

    ServerStats getServerStats(Server server);

    Map<String, String> additionalInfo();
    
    int getMinAvailableServerCount();
    
    ScheduledExecutorService getCheckHealthExecutorService();
}