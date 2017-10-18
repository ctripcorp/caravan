package com.ctrip.soa.caravan.ribbon.serversource.manager;

import com.ctrip.soa.caravan.ribbon.LoadBalancer;
import com.ctrip.soa.caravan.ribbon.LoadBalancerConfig;
import com.ctrip.soa.caravan.ribbon.LoadBalancerManager;
import com.ctrip.soa.caravan.ribbon.LoadBalancerRequestConfig;
import com.ctrip.soa.caravan.ribbon.LoadBalancerRoute;
import com.ctrip.soa.caravan.ribbon.Server;
import com.ctrip.soa.caravan.ribbon.ServerGroup;
import com.ctrip.soa.caravan.ribbon.TestUtils;
import com.ctrip.soa.caravan.ribbon.loadbalancer.DefaultLoadBalancer;
import com.ctrip.soa.caravan.ribbon.serversource.DefaultDynamicServerSource;
import com.ctrip.soa.caravan.ribbon.serversource.filter.ServerSourceFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by w.jian on 2016/7/18.
 */
public class DefaultServerSourceManagerTest {

    final String managerId = "soa";

    @Test
    public void backupRestoreTest() {

        String lbId = Thread.currentThread().getStackTrace()[1].getMethodName();
        String backupFileName = "/opt/data/" + managerId + "." + lbId;

        File file = new File(backupFileName);
        file.delete();

        List<Server> servers = new ArrayList<>();
        for (int i = 0; i < 10; i++)
        {
            Server server = new Server.Builder().setServerId("server_" + i).setIsAlive(i % 2 == 0).build();
            servers.add(server);
        }
        ServerGroup serverGroup = ServerGroup.newBuilder().setGroupId("default").setServers(servers).build();
        List<LoadBalancerRoute> expected = Lists.newArrayList(LoadBalancerRoute.newBuilder().setRouteId(lbId).setServerGroups(Lists.newArrayList(serverGroup)).build());

        LoadBalancerManager factory = LoadBalancerManager.getManager(managerId, TestUtils.getLoadBalancerManagerConfig());
        DefaultDynamicServerSource serverSource = DefaultDynamicServerSource.fromServers(lbId, servers);
        LoadBalancerConfig config = new LoadBalancerConfig.Builder().setServerSource(serverSource).build();
        final LoadBalancer loadBalancer = factory.getLoadBalancer(lbId, config);
        DefaultLoadBalancer defaultLoadBalancer = (DefaultLoadBalancer)loadBalancer;

        ServerSourceManager manager = defaultLoadBalancer.getLoadBalancerContext().serverSourceManager();
        manager.backup(expected);
        List<LoadBalancerRoute> actual = manager.restore();

        file.delete();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void remoteServerListFirstTest() throws IOException {

        int serverCount = 10;
        final String lbId = Thread.currentThread().getStackTrace()[1].getMethodName();
        final String backupFileName = "/opt/data/" + managerId + "." + lbId;
        final LoadBalancerRequestConfig loadBalancerRequestConfig = new LoadBalancerRequestConfig(lbId);

        File file = new File(backupFileName);
        if (!file.exists())
            file.createNewFile();

        List<Server> servers = new ArrayList<>();
        for (int i = 0; i < serverCount; i++) {
            HashMap<String, String> metadata = new HashMap<>();
            metadata.put("Index", Integer.toString(i));
            servers.add(Server.newBuilder().setServerId("server_" + i).setMetadata(metadata).build());
        }
        DefaultDynamicServerSource localSource = DefaultDynamicServerSource.fromServers(lbId, servers);
        new ObjectMapper().writeValue(file, localSource.getLoadBalancerRoutes());

        List<Server> remoteServers = new ArrayList<>();
        for (int i = serverCount; i < serverCount * 2; i++) {
            HashMap<String, String> metadata = new HashMap<>();
            metadata.put("Index", Integer.toString(i));
            remoteServers.add(Server.newBuilder().setServerId("server_" + i).setMetadata(metadata).build());
        }

        LoadBalancerManager factory = LoadBalancerManager.getManager(managerId, TestUtils.getLoadBalancerManagerConfig());
        DefaultDynamicServerSource remoteSource = DefaultDynamicServerSource.fromServers(lbId, remoteServers);
        LoadBalancerConfig config = LoadBalancerConfig.newBuilder().setServerSource(remoteSource).build();
        final LoadBalancer loadBalancer = factory.getLoadBalancer(lbId, config);
        DefaultLoadBalancer defaultLoadBalancer = (DefaultLoadBalancer)loadBalancer;

        file.delete();

        ServerSourceFilter serverSourceFilter = defaultLoadBalancer.getLoadBalancerContext().serverSourceFilter();
        List<Server> allServers = serverSourceFilter.getLoadBalancerRoute(loadBalancerRequestConfig).getServers();
        for (Server server : allServers) {
            int index = Integer.parseInt(server.getMetadata().get("Index"));
            Assert.assertTrue(index >= serverCount);
        }
    }
    
    @Test
    public void serverSourceRestoreEventTest () throws IOException {
        final int serverCount = 10;
        final String lbId = Thread.currentThread().getStackTrace()[1].getMethodName();
        final String backupFileName = "/opt/data/" + managerId + "." + lbId;
        final LoadBalancerRequestConfig loadBalancerRequestConfig = new LoadBalancerRequestConfig(lbId);
    
        File file = new File(backupFileName);
        if (!file.exists())
            file.createNewFile();
    
        List<Server> servers = new ArrayList<>();
        for (int i = 0; i < serverCount; i++) {
            HashMap<String, String> metadata = new HashMap<>();
            metadata.put("Index", Integer.toString(i));
            servers.add(Server.newBuilder().setServerId("server_" + i).setMetadata(metadata).build());
        }
        DefaultDynamicServerSource localSource = DefaultDynamicServerSource.fromServers(lbId, servers);
        new ObjectMapper().writeValue(file, localSource.getLoadBalancerRoutes());
    
        final List<Server> remoteServers = new ArrayList<>();
        ServerSourceRestoreListener restoreListener = new ServerSourceRestoreListener() {
            @Override
            public void onServerSourceRestore(ServerSourceRestoreEvent event) {
                List<LoadBalancerRoute> newRoutes = new ArrayList<>(event.getRoutes());
                for (LoadBalancerRoute route : newRoutes) {
                    for (Server server : route.getServers()) {
                        int index = Integer.parseInt(server.getMetadata().get("Index"));
                        server.getMetadata().put("Index", Integer.toString(index + serverCount));
                    }
                }
                event.setRoutes(newRoutes);
            }
        };
        
        LoadBalancerManager factory = LoadBalancerManager.getManager(managerId, TestUtils.getLoadBalancerManagerConfig());
        DefaultDynamicServerSource remoteSource = DefaultDynamicServerSource.fromServers(lbId, remoteServers);
        LoadBalancerConfig config = LoadBalancerConfig.newBuilder()
                .setServerSource(remoteSource)
                .setServerSourceRestoreListener(restoreListener)
                .build();
        final LoadBalancer loadBalancer = factory.getLoadBalancer(lbId, config);
        DefaultLoadBalancer defaultLoadBalancer = (DefaultLoadBalancer)loadBalancer;
    
        file.delete();
    
        ServerSourceFilter serverSourceFilter = defaultLoadBalancer.getLoadBalancerContext().serverSourceFilter();
        List<Server> allServers = serverSourceFilter.getLoadBalancerRoute(loadBalancerRequestConfig).getServers();
        for (Server server : allServers) {
            int index = Integer.parseInt(server.getMetadata().get("Index"));
            Assert.assertTrue(index >= serverCount);
        }
    }
}
