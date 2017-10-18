package com.ctrip.soa.caravan.ribbon.rule;

import com.ctrip.soa.caravan.ribbon.Server;
import com.ctrip.soa.caravan.ribbon.ServerGroup;
import com.ctrip.soa.caravan.ribbon.algorithm.DefaultRoundRobinAlgorithm;
import com.ctrip.soa.caravan.ribbon.algorithm.RoundRobinAlgorithm;

/**
 * Created by w.jian on 2017/3/5.
 */
class RoundRobinContext {

    private ServerGroup serverGroup;
    private RoundRobinAlgorithm<Server> roundRobinAlgorithm;

    public RoundRobinContext(ServerGroup serverGroup) {
        this.serverGroup = serverGroup;
        this.roundRobinAlgorithm = new DefaultRoundRobinAlgorithm<>();
    }

    public ServerGroup getServerGroup() {
        return serverGroup;
    }

    public Server choose() {
        return roundRobinAlgorithm.choose(serverGroup.getAvailableServers());
    }
}
