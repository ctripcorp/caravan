package com.ctrip.soa.caravan.ribbon.loadbalancer;

import com.ctrip.soa.caravan.ribbon.LoadBalancer;
import com.ctrip.soa.caravan.ribbon.LoadBalancerRequestConfig;
import com.ctrip.soa.caravan.ribbon.LoadBalancerRequestContext;
import com.ctrip.soa.caravan.ribbon.LoadBalancerRoute;
import com.ctrip.soa.caravan.ribbon.Server;
import com.ctrip.soa.caravan.ribbon.util.LogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by w.jian on 2016/6/15.
 */
public class DefaultLoadBalancer implements LoadBalancer {

    private static Logger _logger = LoggerFactory.getLogger(DefaultLoadBalancer.class);
    
    private LoadBalancerContext _loadBalancerContext;

    public DefaultLoadBalancer(LoadBalancerContext loadBalancerContext) {
        _loadBalancerContext = loadBalancerContext;
    }

    @Override
    public LoadBalancerRequestContext getRequestContext(LoadBalancerRequestConfig loadBalancerRequestConfig) {
        LoadBalancerRoute route = _loadBalancerContext.serverSourceFilter().getLoadBalancerRoute(loadBalancerRequestConfig);
        if (route == null) {
            LogUtil.error(_logger, "No matching route for \n" + loadBalancerRequestConfig, _loadBalancerContext.additionalInfo());
            return null;
        }
        Server server = _loadBalancerContext.getLoadBalancerRule(route.getRouteId()).choose(route);
        return server == null ? null : new DefaultLoadBalancerRequestContext(server, _loadBalancerContext);
    }

    public LoadBalancerContext getLoadBalancerContext() {
        return _loadBalancerContext;
    }
}
