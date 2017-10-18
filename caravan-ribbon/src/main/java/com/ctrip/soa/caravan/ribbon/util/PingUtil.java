package com.ctrip.soa.caravan.ribbon.util;

import com.ctrip.soa.caravan.common.value.checker.NullArgumentChecker;
import com.ctrip.soa.caravan.ribbon.Server;
import com.ctrip.soa.caravan.ribbon.loadbalancer.LoadBalancerContext;
import org.slf4j.Logger;

/**
 * Created by w.jian on 2016/11/7.
 */
public class PingUtil {

    private Logger _logger;
    private LoadBalancerContext _loadBalancerContext;

    public PingUtil(LoadBalancerContext loadBalancerContext, Logger logger) {
        NullArgumentChecker.DEFAULT.check(loadBalancerContext, "loadBalancerContext");
        NullArgumentChecker.DEFAULT.check(logger, "logger");

        _loadBalancerContext = loadBalancerContext;
        _logger = logger;
    }

    public boolean hasPing() {
        return _loadBalancerContext.ping() != null;
    }

    public boolean isAlive(Server server) {
        try {
            if (!hasPing()) {
                return true;
            }

            return _loadBalancerContext.ping().isAlive(server);
        } catch (Throwable t) {
            String message = String.format("Error occurred while ping %s.", server.toString());
            LogUtil.warn(_logger, message, t, _loadBalancerContext.additionalInfo());
            return false;
        }
    }
}
