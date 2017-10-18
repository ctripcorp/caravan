package com.ctrip.soa.caravan.ribbon.serversource;

import com.ctrip.soa.caravan.common.value.checker.NullArgumentChecker;
import com.ctrip.soa.caravan.common.value.checker.StringArgumentChecker;
import com.ctrip.soa.caravan.ribbon.LoadBalancerRoute;
import com.ctrip.soa.caravan.ribbon.Server;
import com.ctrip.soa.caravan.ribbon.ServerGroup;
import com.ctrip.soa.caravan.ribbon.ServerSource;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by w.jian on 2016/7/21.
 */
public class DefaultServerSource implements ServerSource {

    protected List<LoadBalancerRoute> routes;

    protected DefaultServerSource() { }

    @Override
    public List<LoadBalancerRoute> getLoadBalancerRoutes() {
        if(routes == null)
            routes = new ArrayList<>();
        return routes;
    }

    public static DefaultServerSource fromServers(String routeId, Server server) {
        return fromServers(routeId, Lists.newArrayList(server));
    }

    public static DefaultServerSource fromServers(String routeId, List<Server> servers) {
        StringArgumentChecker.DEFAULT.check(routeId, "routeId");
        NullArgumentChecker.DEFAULT.check(servers, "servers");

        ServerGroup serverGroup = ServerGroup.newBuilder()
                .setGroupId("default")
                .setServers(servers)
                .build();

        DefaultServerSource serverSource = new DefaultServerSource();
        serverSource.routes = new ArrayList<>();
        serverSource.routes.add(LoadBalancerRoute.newBuilder().setRouteId(routeId).setServerGroups(Lists.newArrayList(serverGroup)).build());
        return serverSource;
    }

    public static DefaultServerSource fromServerGroups(String routeId, ServerGroup serverGroup) {
        return fromServerGroups(routeId, Lists.newArrayList(serverGroup));
    }

    public static DefaultServerSource fromServerGroups(String routeId, List<ServerGroup> serverGroups) {
        StringArgumentChecker.DEFAULT.check(routeId, "routeId");
        NullArgumentChecker.DEFAULT.check(serverGroups, "serverGroups");

        DefaultServerSource serverSource = new DefaultServerSource();
        serverSource.routes = new ArrayList<>();
        serverSource.routes.add(LoadBalancerRoute.newBuilder().setRouteId(routeId).setServerGroups(serverGroups).build());
        return serverSource;
    }
}
