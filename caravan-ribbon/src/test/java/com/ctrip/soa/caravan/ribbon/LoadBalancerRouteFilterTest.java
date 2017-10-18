package com.ctrip.soa.caravan.ribbon;

import com.ctrip.soa.caravan.ribbon.loadbalancer.DefaultLoadBalancer;
import com.ctrip.soa.caravan.ribbon.serversource.DefaultDynamicServerSource;
import com.ctrip.soa.caravan.ribbon.serversource.filter.ServerSourceFilter;
import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by w.jian on 2017/5/27.
 */
public class LoadBalancerRouteFilterTest {
    
    static final String managerId = "soa";
    
    @Test
    public void canReplaceRouteTest() {
        int repeat = 2;
        int serverCount = 10;
        final String lbId = Thread.currentThread().getStackTrace()[1].getMethodName();
    
        List<Server> servers = new ArrayList<>();
        for (int i = 0; i < serverCount; i++) {
            servers.add(Server.newBuilder().setServerId("server_" + i).build());
        }
    
        List<LoadBalancerRoute> routes = new ArrayList<>();
        List<ServerGroup> groups = Lists.newArrayList(new ServerGroup("default", 1, servers, null));
        routes.add(new LoadBalancerRoute("default", groups));
        
        List<Server> newServers = Lists.newArrayList(servers.get(0));
        List<ServerGroup> newGroups = Lists.newArrayList(new ServerGroup("default", 1, newServers, null));
        final LoadBalancerRoute newRoute = new LoadBalancerRoute("newRoute", newGroups);
        
        final boolean[] filterExecuted = new boolean[1];
        SeekRouteListener listener = new SeekRouteListener() {
            @Override
            public void onSeekRoute(SeekRouteEvent event) {
                filterExecuted[0] = true;
                event.setRoute(newRoute);
            }
        };
        
        final LoadBalancerRequestConfig requestConfig = new LoadBalancerRequestConfig("default");
    
        Ping ping = new Ping() {
            @Override
            public boolean isAlive(Server server) {
                return true;
            }
        };
        LoadBalancerManager factory = LoadBalancerManager.getManager(managerId, TestUtils.getLoadBalancerManagerConfig());
        DefaultDynamicServerSource serverSource = DefaultDynamicServerSource.fromRoutes(routes);
        LoadBalancerConfig config = LoadBalancerConfig.newBuilder()
                .setPing(ping)
                .setServerSource(serverSource)
                .setSeekRouteListener(listener)
                .build();
        final LoadBalancer loadBalancer = factory.getLoadBalancer(lbId, config);
        DefaultLoadBalancer defaultLoadBalancer = (DefaultLoadBalancer)loadBalancer;
        ServerSourceFilter serverSourceFilter = defaultLoadBalancer.getLoadBalancerContext().serverSourceFilter();
    
        Assert.assertEquals(servers.size(), serverSourceFilter.getLoadBalancerRoutes().get(0).getServers().size());
        Assert.assertEquals(servers.size(), serverSourceFilter.getLoadBalancerRoutes().get(0).getAvailableServers().size());
    
        HashMap<Server, AtomicInteger> serverCountMap = new HashMap<>();
        for (int i = 0; i < servers.size() * repeat; i++) {
            LoadBalancerRequestContext requestContext = loadBalancer.getRequestContext(requestConfig);
            if (serverCountMap.containsKey(requestContext.getServer()))
                serverCountMap.get(requestContext.getServer()).incrementAndGet();
            else
                serverCountMap.put(requestContext.getServer(), new AtomicInteger(1));
        }
    
        Assert.assertEquals(true, filterExecuted[0]);
        Assert.assertEquals(1, serverCountMap.size());
        for (AtomicInteger atomicInteger : serverCountMap.values()) {
            Assert.assertEquals(servers.size() * repeat, atomicInteger.intValue());
        }
    }
}
