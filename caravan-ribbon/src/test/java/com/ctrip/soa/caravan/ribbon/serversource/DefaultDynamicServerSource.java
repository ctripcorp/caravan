package com.ctrip.soa.caravan.ribbon.serversource;

import com.ctrip.soa.caravan.common.value.checker.CollectionArgumentChecker;
import com.ctrip.soa.caravan.common.value.checker.NullArgumentChecker;
import com.ctrip.soa.caravan.common.value.checker.StringArgumentChecker;
import com.ctrip.soa.caravan.ribbon.*;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by w.jian on 2016/6/15.
 */
public class DefaultDynamicServerSource extends DefaultServerSource implements DynamicServerSource {

    private static Logger _logger = LoggerFactory.getLogger(DefaultDynamicServerSource.class);

    private List<ServerSourceChangeListener> _listeners = new ArrayList<>();

    public void setLoadBalanceRoutes(List<LoadBalancerRoute> newRoutes) {
        CollectionArgumentChecker.DEFAULT.check(newRoutes, "newRoutes");

        if (routes == newRoutes)
            return;

        if (newRoutes == null)
            return;

        routes = newRoutes;
        raiseServerSourceChangeEvent();
    }

    public void setServers(String routeId, List<Server> servers) {
        ServerGroup serverGroup = ServerGroup.newBuilder()
                .setGroupId("default")
                .setServers(servers)
                .setWeight(1)
                .build();
        setServerGroups(routeId, Lists.newArrayList(serverGroup));
    }

    public void setServerGroups(String routeId, List<ServerGroup> serverGroups) {
        StringArgumentChecker.DEFAULT.check(routeId, "routeId");
        NullArgumentChecker.DEFAULT.check(serverGroups, "serverGroups");

        List<LoadBalancerRoute> newRoutes = new ArrayList<>();
        newRoutes.add(LoadBalancerRoute.newBuilder().setRouteId(routeId).setServerGroups(serverGroups).build());
        setLoadBalanceRoutes(newRoutes);
    }

    @Override
    public synchronized void registerServerSourceChangeListener(ServerSourceChangeListener listener) {
        NullArgumentChecker.DEFAULT.check(listener, "listener");
        _listeners.add(listener);
    }

    private void raiseServerSourceChangeEvent() {
        if(_listeners.size() == 0)
            return;

        ServerSourceChangeEvent event = new ServerSourceChangeEvent() { };
        for (ServerSourceChangeListener listener :_listeners) {
            try {
                listener.onChange(event);
            } catch (Throwable t) {
                _logger.warn("Error occurred while raising server source change event", t);
            }
        }
    }

    public static DefaultDynamicServerSource fromServers(String routeId, Server server) {
        return fromServers(routeId, Lists.newArrayList(server));
    }

    public static DefaultDynamicServerSource fromServers(String routeId, List<Server> servers) {
        StringArgumentChecker.DEFAULT.check(routeId, "routeId");
        NullArgumentChecker.DEFAULT.check(servers, "servers");

        ServerGroup serverGroup = ServerGroup.newBuilder()
                .setGroupId("default")
                .setWeight(1)
                .setServers(servers)
                .build();

        DefaultDynamicServerSource serverSource = new DefaultDynamicServerSource();
        serverSource.routes = new ArrayList<>();
        serverSource.routes.add(LoadBalancerRoute.newBuilder().setRouteId(routeId).setServerGroups(Lists.newArrayList(serverGroup)).build());
        return serverSource;
    }

    public static DefaultDynamicServerSource fromServerGroups(String routeId, ServerGroup serverGroup) {
        return fromServerGroups(routeId, Lists.newArrayList(serverGroup));
    }

    public static DefaultDynamicServerSource fromServerGroups(String routeId, List<ServerGroup> serverGroups) {
        StringArgumentChecker.DEFAULT.check(routeId, "routeId");
        NullArgumentChecker.DEFAULT.check(serverGroups, "serverGroups");

        DefaultDynamicServerSource serverSource = new DefaultDynamicServerSource();
        serverSource.routes = new ArrayList<>();
        serverSource.routes.add(LoadBalancerRoute.newBuilder().setRouteId(routeId).setServerGroups(serverGroups).build());
        return serverSource;
    }

    public static DefaultDynamicServerSource fromRoutes(List<LoadBalancerRoute> routes) {
        NullArgumentChecker.DEFAULT.check(routes, "routes");

        DefaultDynamicServerSource serverSource = new DefaultDynamicServerSource();
        serverSource.routes = routes;
        return serverSource;
    }
}
