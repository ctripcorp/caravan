package com.ctrip.soa.caravan.ribbon.rule;

import com.ctrip.soa.caravan.common.delegate.Func;
import com.ctrip.soa.caravan.common.value.CollectionValues;
import com.ctrip.soa.caravan.ribbon.*;
import com.ctrip.soa.caravan.ribbon.algorithm.DefaultRoundRobinAlgorithm;
import com.ctrip.soa.caravan.ribbon.algorithm.RoundRobinAlgorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static com.ctrip.soa.caravan.common.value.ConcurrentHashMapValues.getOrAddWithLock;

/**
 * Created by w.jian on 2016/6/15.
 */
public class RoundRobinRule implements LoadBalancerRule {

    protected volatile LoadBalancerRoute loadBalancerRoute;
    protected volatile List<ServerGroup> serverGroups;
    protected RoundRobinAlgorithm<ServerGroup> roundRobinAlgorithm;
    protected volatile ConcurrentHashMap<String, RoundRobinContext> serverContexts;

    @Override
    public String getRuleId() {
        return DefaultLoadBalancerRuleFactoryManager.ROUND_ROBIN_RULE_NAME;
    }

    @Override
    public String getDescription() {
        return DefaultLoadBalancerRuleFactoryManager.ROUND_ROBIN_RULE_DESCRIPTION;
    }

    public RoundRobinRule() {
        serverContexts = new ConcurrentHashMap<>();
        roundRobinAlgorithm = new DefaultRoundRobinAlgorithm<>();
    }

    @Override
    public Server choose(final LoadBalancerRoute route) {
        if (route == null || CollectionValues.isNullOrEmpty(route.getServerGroups()))
            return null;

        internalBuildServerGroupContext(route);
        ServerGroup serverGroup = null;
        for (int i = 0; i < serverGroups.size(); i++) {
            serverGroup = roundRobinAlgorithm.choose(serverGroups);
            if (serverGroup.getAvailableServers().size() > 0) {
                break;
            }
        }
        if (serverGroup == null)
            return null;

        final ServerGroup finalServerGroup = serverGroup;
        String groupId = serverGroup.getGroupId();
        RoundRobinContext serverContext = getOrAddWithLock(serverContexts, groupId, new Func<RoundRobinContext>() {
            @Override
            public RoundRobinContext execute() {
                return new RoundRobinContext(finalServerGroup);
            }
        });
        return serverContext.choose();
    }

    protected final void internalBuildServerGroupContext(LoadBalancerRoute route) {
        if (loadBalancerRoute != route) {
            synchronized (this) {
                if (loadBalancerRoute != route) {
                    serverGroups = buildServerGroupContext(route);
                    serverContexts = new ConcurrentHashMap<>();
                    loadBalancerRoute = route;
                }
            }
        }
    }

    protected List<ServerGroup> buildServerGroupContext(LoadBalancerRoute route) {
        List<ServerGroup> newServerGroups = new ArrayList<>();
        for (ServerGroup item : route.getServerGroups()) {
            newServerGroups.add(item);
        }
        return newServerGroups;
    }
}
