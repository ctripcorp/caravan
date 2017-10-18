package com.ctrip.soa.caravan.ribbon.loadbalancer;

import com.ctrip.soa.caravan.common.value.checker.NullArgumentChecker;
import com.ctrip.soa.caravan.ribbon.LoadBalancerRequestContext;
import com.ctrip.soa.caravan.ribbon.Server;
import com.ctrip.soa.caravan.ribbon.server.ServerStats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by w.jian on 2016/7/13.
 */
class DefaultLoadBalancerRequestContext implements LoadBalancerRequestContext {

    private static Logger _logger = LoggerFactory.getLogger(DefaultLoadBalancerRequestContext.class);

    private Server _server;
    private ServerStats _serverStats;

    public DefaultLoadBalancerRequestContext(Server server, LoadBalancerContext loadBalancerContext) {
        NullArgumentChecker.DEFAULT.check(server, "server");
        NullArgumentChecker.DEFAULT.check(loadBalancerContext, "loadBalancerContext");
        _server = server;
        _serverStats = loadBalancerContext.getServerStats(server);
    }

    @Override
    public Server getServer() {
        return _server;
    }

    @Override
    public void markServerAvailable() {
        if(_serverStats == null) {
            _logger.warn("ServerContext is null");
            return;
        }
        _serverStats.addAvailableCount();
    }

    @Override
    public void markServerUnavailable() {
        if(_serverStats == null) {
            _logger.warn("ServerContext is null");
            return;
        }
        _serverStats.addUnavailableCount();
    }
}
