package com.ctrip.soa.caravan.ribbon.serversource.filter;

import com.ctrip.soa.caravan.ribbon.*;
import com.ctrip.soa.caravan.ribbon.loadbalancer.DefaultLoadBalancer;
import com.ctrip.soa.caravan.ribbon.serversource.DefaultDynamicServerSource;
import com.ctrip.soa.caravan.ribbon.serversource.manager.ServerSourceManager;
import com.ctrip.soa.caravan.ribbon.util.LoadBalancerRoutes;
import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by w.jian on 2016/7/26.
 */
public class DefaultServerSourceFilterTest {
    
    static final String managerId = "soa";
    static final Logger _logger = LoggerFactory.getLogger(DefaultServerSourceFilter.class);

    @Test
    public void updateServerList_NotUpdateEmptyList_Test() {
        int serverCount = 10;
        final String lbId = Thread.currentThread().getStackTrace()[1].getMethodName();
        final LoadBalancerRequestConfig loadBalancerRequestConfig = new LoadBalancerRequestConfig(lbId);

        final List<Server> servers = new ArrayList<>();
        for (int i = 0; i < serverCount; i++) {
            servers.add(Server.newBuilder().setServerId("server_" + i).build());
        }

        Ping ping = new Ping() {
            @Override
            public boolean isAlive(Server server) {
                return true;
            }
        };
        LoadBalancerManager factory = LoadBalancerManager.getManager(managerId, TestUtils.getLoadBalancerManagerConfig());
        DefaultDynamicServerSource serverSource = DefaultDynamicServerSource.fromServers(lbId, servers);
        LoadBalancerConfig config = new LoadBalancerConfig.Builder().setPing(ping).setServerSource(serverSource).build();
        final LoadBalancer loadBalancer = factory.getLoadBalancer(lbId, config);
        DefaultLoadBalancer defaultLoadBalancer = (DefaultLoadBalancer)loadBalancer;

        ServerSourceFilter serverSourceFilter = defaultLoadBalancer.getLoadBalancerContext().serverSourceFilter();
        Assert.assertEquals(serverCount, serverSourceFilter.getLoadBalancerRoute(loadBalancerRequestConfig).getServers().size());

        final boolean[] eventRaised = new boolean[] { false };
        serverSourceFilter.addServerContextChangeListener(new ServerContextChangeListener() {
            @Override
            public void onChange(ServerContextChangeEvent event) {
                eventRaised[0] = true;
            }
        });
        servers.clear();
        serverSource.setServers(lbId, servers);
        Assert.assertEquals(serverCount, serverSourceFilter.getLoadBalancerRoute(loadBalancerRequestConfig).getServers().size());
    }

    @Test
    public void updateServerListTest() {
        final int serverCount = 10;
        final String lbId = Thread.currentThread().getStackTrace()[1].getMethodName();
        final LoadBalancerRequestConfig loadBalancerRequestConfig = new LoadBalancerRequestConfig(lbId);

        List<Server> servers = new ArrayList<>();
        for (int i = 0; i < serverCount; i++) {
            servers.add(Server.newBuilder().setServerId("server_" + i).build());
        }
        
        Ping ping = new Ping() {
            @Override
            public boolean isAlive(Server server) {
                return true;
            }
        };
        LoadBalancerManager factory = LoadBalancerManager.getManager(managerId, TestUtils.getLoadBalancerManagerConfig());
        DefaultDynamicServerSource serverSource = DefaultDynamicServerSource.fromServers(lbId, servers);
        LoadBalancerConfig config = new LoadBalancerConfig.Builder().setPing(ping).setServerSource(serverSource).build();
        final LoadBalancer loadBalancer = factory.getLoadBalancer(lbId, config);
        DefaultLoadBalancer defaultLoadBalancer = (DefaultLoadBalancer)loadBalancer;

        ServerSourceFilter serverSourceFilter = defaultLoadBalancer.getLoadBalancerContext().serverSourceFilter();
        Assert.assertEquals(serverCount, serverSourceFilter.getLoadBalancerRoute(loadBalancerRequestConfig).getServers().size());

        servers = new ArrayList<>();
        for (int i = 0; i < serverCount * 2; i++) {
            servers.add(Server.newBuilder().setServerId("server_" + i).build());
        }
        serverSource.setServers(lbId, servers);
        Assert.assertEquals(serverCount * 2, serverSourceFilter.getLoadBalancerRoute(loadBalancerRequestConfig).getServers().size());
    }

    @Test
    public void updateServerList_AlwaysChange_Test() {
        int serverCount = 10;
        final String lbId = Thread.currentThread().getStackTrace()[1].getMethodName();
        final LoadBalancerRequestConfig loadBalancerRequestConfig = new LoadBalancerRequestConfig(lbId);

        final List<Server> servers = new ArrayList<>();
        for (int i = 0; i < serverCount; i++) {
            servers.add(Server.newBuilder().setServerId("server_" + i).build());
        }

        Ping ping = new Ping() {
            @Override
            public boolean isAlive(Server server) {
                return true;
            }
        };
        LoadBalancerManager factory = LoadBalancerManager.getManager(managerId, TestUtils.getLoadBalancerManagerConfig());
        DefaultDynamicServerSource serverSource = DefaultDynamicServerSource.fromServers(lbId, servers);
        LoadBalancerConfig config = new LoadBalancerConfig.Builder().setPing(ping).setServerSource(serverSource).build();
        final LoadBalancer loadBalancer = factory.getLoadBalancer(lbId, config);
        DefaultLoadBalancer defaultLoadBalancer = (DefaultLoadBalancer)loadBalancer;

        ServerSourceFilter serverSourceFilter = defaultLoadBalancer.getLoadBalancerContext().serverSourceFilter();
        Assert.assertEquals(serverCount, serverSourceFilter.getLoadBalancerRoute(loadBalancerRequestConfig).getServers().size());

        final boolean[] eventRaised = new boolean[] { false };
        serverSource.registerServerSourceChangeListener(new ServerSourceChangeListener() {
            @Override
            public void onChange(ServerSourceChangeEvent event) {
                eventRaised[0] = true;
            }
        });
        serverSource.setServers(lbId, servers);
        Assert.assertEquals(serverCount, serverSourceFilter.getLoadBalancerRoute(loadBalancerRequestConfig).getServers().size());
        
        LoadBalancerRoute targetRoute = serverSourceFilter.getLoadBalancerRoute(loadBalancerRequestConfig);
        LoadBalancerRoute originRoute = serverSourceFilter.getLoadBalancerRoutes().get(0);
        Assert.assertSame(originRoute, targetRoute);
        
        Assert.assertTrue(eventRaised[0]);
    }

    @Test
    public void updateServerList_RetainStatus_Test() {
        int serverCount = 10;
        final String lbId = Thread.currentThread().getStackTrace()[1].getMethodName();
        final LoadBalancerRequestConfig loadBalancerRequestConfig = new LoadBalancerRequestConfig(lbId);

        final List<Server> servers = new ArrayList<>();
        for (int i = 0; i < serverCount; i++) {
            HashMap<String, String> metadata = new HashMap<>();
            metadata.put("Hello", "World");
            servers.add(Server.newBuilder().setServerId("server_" + i).setMetadata(metadata).build());
        }

        Ping ping = new Ping() {
            @Override
            public boolean isAlive(Server server) {
                return true;
            }
        };
        LoadBalancerManager factory = LoadBalancerManager.getManager(managerId, TestUtils.getLoadBalancerManagerConfig());
        DefaultDynamicServerSource serverSource = DefaultDynamicServerSource.fromServers(lbId, servers);
        LoadBalancerConfig config = new LoadBalancerConfig.Builder().setPing(ping).setServerSource(serverSource).build();
        final LoadBalancer loadBalancer = factory.getLoadBalancer(lbId, config);
        DefaultLoadBalancer defaultLoadBalancer = (DefaultLoadBalancer)loadBalancer;

        ServerSourceFilter serverSourceFilter = defaultLoadBalancer.getLoadBalancerContext().serverSourceFilter();
        Assert.assertEquals(serverCount, serverSourceFilter.getLoadBalancerRoute(loadBalancerRequestConfig).getServers().size());

        for (int i = serverCount; i < serverCount * 2; i++) {
            HashMap<String, String> metadata = new HashMap<>();
            metadata.put("Hello", "There");
            servers.add(Server.newBuilder().setServerId("server_" + i).setMetadata(metadata).build());
        }
        serverSource.setServers(lbId, servers);
        List<Server> serverList = serverSourceFilter.getLoadBalancerRoute(loadBalancerRequestConfig).getServers();
        Assert.assertEquals(serverCount * 2, serverList.size());
        for (int i = 0; i < serverList.size(); i++) {
            if (i < serverCount) {
                Assert.assertEquals("World", serverList.get(i).getMetadata().get("Hello"));
            } else {
                Assert.assertEquals("There", serverList.get(i).getMetadata().get("Hello"));
            }
        }
    }

    @Test
    public void updateServerList_UnavailableServer_Test() {
        final int serverCount = 10;
        final String lbId = Thread.currentThread().getStackTrace()[1].getMethodName();
        final LoadBalancerRequestConfig loadBalancerRequestConfig = new LoadBalancerRequestConfig(lbId);

        final List<Server> servers = new ArrayList<>();
        for (int i = 0; i < serverCount; i++) {
            HashMap<String, String> metadata = new HashMap<>();
            metadata.put("Index", Integer.toString(i));
            servers.add(Server.newBuilder().setServerId("server_" + i).setMetadata(metadata).build());
        }

        Ping ping = new Ping() {
            @Override
            public boolean isAlive(Server server) {
                int index = Integer.parseInt(server.getMetadata().get("Index"));
                return index < serverCount || index % 2 == 0;
            }
        };
        LoadBalancerManager factory = LoadBalancerManager.getManager(managerId, TestUtils.getLoadBalancerManagerConfig());
        DefaultDynamicServerSource serverSource = DefaultDynamicServerSource.fromServers(lbId, servers);
        LoadBalancerConfig config = new LoadBalancerConfig.Builder().setPing(ping).setServerSource(serverSource).build();
        final LoadBalancer loadBalancer = factory.getLoadBalancer(lbId, config);
        DefaultLoadBalancer defaultLoadBalancer = (DefaultLoadBalancer)loadBalancer;

        ServerSourceFilter serverSourceFilter = defaultLoadBalancer.getLoadBalancerContext().serverSourceFilter();
        Assert.assertEquals(serverCount, serverSourceFilter.getLoadBalancerRoute(loadBalancerRequestConfig).getServers().size());

        for (int i = serverCount; i < serverCount * 2; i++) {
            HashMap<String, String> metadata = new HashMap<>();
            metadata.put("Index", Integer.toString(i));
            servers.add(Server.newBuilder().setServerId("server_" + i).setMetadata(metadata).build());
        }
        serverSource.setServers(lbId, servers);

        List<Server> allServers = serverSourceFilter.getLoadBalancerRoute(loadBalancerRequestConfig).getServers();
        Assert.assertEquals(serverCount * 2, allServers.size());
        List<Server> availableServers = serverSourceFilter.getLoadBalancerRoute(loadBalancerRequestConfig).getAvailableServers();
        Assert.assertEquals(serverCount + serverCount / 2, availableServers.size());

        List<Server> serverList = serverSourceFilter.getLoadBalancerRoute(loadBalancerRequestConfig).getServers();
        for (Server server : serverList) {
            int index = Integer.parseInt(server.getMetadata().get("Index"));
            if (index < serverCount) {
                Assert.assertTrue(server.isAlive());
            } else {
                Assert.assertEquals(index % 2 == 0, server.isAlive());
            }
        }
    }

    @Test
    public void updateServerList_FirstTimeNotPing_Test() {
        int serverCount = 10;
        final String lbId = Thread.currentThread().getStackTrace()[1].getMethodName();
        final LoadBalancerRequestConfig loadBalancerRequestConfig = new LoadBalancerRequestConfig(lbId);

        final List<Server> servers = new ArrayList<>();
        for (int i = 0; i < serverCount; i++) {
            HashMap<String, String> metadata = new HashMap<>();
            metadata.put("Index", Integer.toString(i));
            servers.add(Server.newBuilder().setServerId("server_" + i).setMetadata(metadata).build());
        }

        final int[] pingCount = new int[] { 0 };
        Ping ping = new Ping() {
            @Override
            public boolean isAlive(Server server) {
                pingCount[0]++;
                return true;
            }
        };
        LoadBalancerManager factory = LoadBalancerManager.getManager(managerId, TestUtils.getLoadBalancerManagerConfig());
        DefaultDynamicServerSource serverSource = DefaultDynamicServerSource.fromServers(lbId, servers);
        LoadBalancerConfig config = new LoadBalancerConfig.Builder().setPing(ping).setServerSource(serverSource).build();
        final LoadBalancer loadBalancer = factory.getLoadBalancer(lbId, config);
        DefaultLoadBalancer defaultLoadBalancer = (DefaultLoadBalancer)loadBalancer;
        ServerSourceFilter serverSourceFilter = defaultLoadBalancer.getLoadBalancerContext().serverSourceFilter();

        Assert.assertEquals(serverCount, serverSourceFilter.getLoadBalancerRoute(loadBalancerRequestConfig).getServers().size());
        Assert.assertEquals(0, pingCount[0]);
    }

    @Test
    public void updateServerList_NotPingMinAvailableServerCount_Test() {
        int serverCount = 10;
        final String lbId = Thread.currentThread().getStackTrace()[1].getMethodName();
        final LoadBalancerRequestConfig loadBalancerRequestConfig = new LoadBalancerRequestConfig(lbId);

        List<Server> servers = new ArrayList<>();
        for (int i = 0; i < serverCount; i++) {
            servers.add(Server.newBuilder().setServerId("server_" + i).build());
        }

        Ping ping = new Ping() {
            @Override
            public boolean isAlive(Server server) {
                return false;
            }
        };
        LoadBalancerManager factory = LoadBalancerManager.getManager(managerId, TestUtils.getLoadBalancerManagerConfig());
        DefaultDynamicServerSource serverSource = DefaultDynamicServerSource.fromServers(lbId, servers);
        LoadBalancerConfig config = new LoadBalancerConfig.Builder().setPing(ping).setServerSource(serverSource).build();
        final LoadBalancer loadBalancer = factory.getLoadBalancer(lbId, config);
        DefaultLoadBalancer defaultLoadBalancer = (DefaultLoadBalancer)loadBalancer;
        ServerSourceFilter serverSourceFilter = defaultLoadBalancer.getLoadBalancerContext().serverSourceFilter();

        Assert.assertEquals(serverCount, serverSourceFilter.getLoadBalancerRoute(loadBalancerRequestConfig).getServers().size());
        Assert.assertEquals(serverCount, serverSourceFilter.getLoadBalancerRoute(loadBalancerRequestConfig).getAvailableServers().size());

        servers = new ArrayList<>();
        int count = defaultLoadBalancer.getLoadBalancerContext().getMinAvailableServerCount();
        Assert.assertEquals(3, count);
        for (int i = 0; i < count; i++) {
            servers.add(Server.newBuilder().setServerId("newServer" + i).build());
        }
        serverSource.setServers(lbId, servers);
        Assert.assertEquals(count, serverSourceFilter.getLoadBalancerRoute(loadBalancerRequestConfig).getServers().size());
        Assert.assertEquals(count, serverSourceFilter.getLoadBalancerRoute(loadBalancerRequestConfig).getAvailableServers().size());
    }
    
    @Test
    public void updateServerList_NotPingExistServer_Test() {
        int serverCount = 10;
        final String lbId = Thread.currentThread().getStackTrace()[1].getMethodName();
        final LoadBalancerRequestConfig loadBalancerRequestConfig = new LoadBalancerRequestConfig(lbId);
        
        List<Server> servers = new ArrayList<>();
        for (int i = 0; i < serverCount; i++) {
            servers.add(Server.newBuilder().setServerId("server_" + i).build());
        }
        
        Ping ping = new Ping() {
            @Override
            public boolean isAlive(Server server) {
                return false;
            }
        };
        LoadBalancerManager factory = LoadBalancerManager.getManager(managerId, TestUtils.getLoadBalancerManagerConfig());
        DefaultDynamicServerSource serverSource = DefaultDynamicServerSource.fromServers(lbId, servers);
        LoadBalancerConfig config = new LoadBalancerConfig.Builder().setPing(ping).setServerSource(serverSource).build();
        final LoadBalancer loadBalancer = factory.getLoadBalancer(lbId, config);
        DefaultLoadBalancer defaultLoadBalancer = (DefaultLoadBalancer)loadBalancer;
        ServerSourceFilter serverSourceFilter = defaultLoadBalancer.getLoadBalancerContext().serverSourceFilter();
        
        Assert.assertEquals(serverCount, serverSourceFilter.getLoadBalancerRoute(loadBalancerRequestConfig).getServers().size());
        Assert.assertEquals(serverCount, serverSourceFilter.getLoadBalancerRoute(loadBalancerRequestConfig).getAvailableServers().size());
        
        servers = new ArrayList<>(servers);
        for (int i = serverCount; i < serverCount * 2; i++) {
            servers.add(Server.newBuilder().setServerId("newServer" + i).build());
        }
        serverSource.setServers(lbId, servers);
        Assert.assertEquals(serverCount * 2, serverSourceFilter.getLoadBalancerRoute(loadBalancerRequestConfig).getServers().size());
        Assert.assertEquals(serverCount, serverSourceFilter.getLoadBalancerRoute(loadBalancerRequestConfig).getAvailableServers().size());
    }
    
    @Test
    public void updateServerList_PullOutServer_Test() {
        int repeat = 2;
        int serverCount = 10;
        final String lbId = Thread.currentThread().getStackTrace()[1].getMethodName();
        final LoadBalancerRequestConfig loadBalancerRequestConfig = new LoadBalancerRequestConfig(lbId);

        List<Server> servers = new ArrayList<>();
        for (int i = 0; i < serverCount; i++) {
            servers.add(Server.newBuilder().setServerId("server_" + i).build());
        }

        Ping ping = new Ping() {
            @Override
            public boolean isAlive(Server server) {
                return true;
            }
        };
        LoadBalancerManager factory = LoadBalancerManager.getManager(managerId, TestUtils.getLoadBalancerManagerConfig());
        DefaultDynamicServerSource serverSource = DefaultDynamicServerSource.fromServers(lbId, servers);
        LoadBalancerConfig config = new LoadBalancerConfig.Builder().setPing(ping).setServerSource(serverSource).build();
        final LoadBalancer loadBalancer = factory.getLoadBalancer(lbId, config);
        DefaultLoadBalancer defaultLoadBalancer = (DefaultLoadBalancer)loadBalancer;
        ServerSourceFilter serverSourceFilter = defaultLoadBalancer.getLoadBalancerContext().serverSourceFilter();

        Assert.assertEquals(serverCount, serverSourceFilter.getLoadBalancerRoute(loadBalancerRequestConfig).getServers().size());
        Assert.assertEquals(serverCount, serverSourceFilter.getLoadBalancerRoute(loadBalancerRequestConfig).getAvailableServers().size());

        LoadBalancerRequestConfig requestConfig = new LoadBalancerRequestConfig(lbId);
        HashMap<Server, AtomicInteger> serverCountMap = new HashMap<>();
        for (int i = 0; i < servers.size() * repeat; i++) {
            LoadBalancerRequestContext requestContext = loadBalancer.getRequestContext(requestConfig);
            if (serverCountMap.containsKey(requestContext.getServer()))
                serverCountMap.get(requestContext.getServer()).incrementAndGet();
            else
                serverCountMap.put(requestContext.getServer(), new AtomicInteger(1));
        }

        for (AtomicInteger atomicInteger : serverCountMap.values()) {
            Assert.assertEquals(2, atomicInteger.intValue());
        }

        servers.remove(0);
        serverSource.setServers(lbId, servers);
        Assert.assertEquals(serverCount - 1, serverSourceFilter.getLoadBalancerRoute(loadBalancerRequestConfig).getServers().size());

        serverCountMap.clear();
        for (int i = 0; i < servers.size() * repeat; i++) {
            LoadBalancerRequestContext requestContext = loadBalancer.getRequestContext(requestConfig);
            if (serverCountMap.containsKey(requestContext.getServer()))
                serverCountMap.get(requestContext.getServer()).incrementAndGet();
            else
                serverCountMap.put(requestContext.getServer(), new AtomicInteger(1));
        }

        for (AtomicInteger atomicInteger : serverCountMap.values()) {
            Assert.assertEquals(2, atomicInteger.intValue());
        }
    }

    @Test
    public void updateServerList_AddNewRoute_Test() {
        int repeat = 2;
        int serverCount = 10;
        final String lbId = Thread.currentThread().getStackTrace()[1].getMethodName();

        List<Server> servers = new ArrayList<>();
        for (int i = 0; i < serverCount; i++) {
            servers.add(Server.newBuilder().setServerId("server_" + i).build());
        }

        List<LoadBalancerRoute> routes = new ArrayList<>();
        routes.add(new LoadBalancerRoute(lbId, Lists.newArrayList(new ServerGroup("default", 1, servers, null))));

        final LoadBalancerRequestConfig requestConfig = new LoadBalancerRequestConfig(lbId);

        Ping ping = new Ping() {
            @Override
            public boolean isAlive(Server server) {
                return true;
            }
        };
        LoadBalancerManager factory = LoadBalancerManager.getManager(managerId, TestUtils.getLoadBalancerManagerConfig());
        DefaultDynamicServerSource serverSource = DefaultDynamicServerSource.fromServers(lbId, servers);
        LoadBalancerConfig config = new LoadBalancerConfig.Builder().setPing(ping).setServerSource(serverSource).build();
        final LoadBalancer loadBalancer = factory.getLoadBalancer(lbId, config);
        DefaultLoadBalancer defaultLoadBalancer = (DefaultLoadBalancer)loadBalancer;
        ServerSourceFilter serverSourceFilter = defaultLoadBalancer.getLoadBalancerContext().serverSourceFilter();

        Assert.assertEquals(servers.size(), serverSourceFilter.getLoadBalancerRoute(requestConfig).getServers().size());
        Assert.assertEquals(servers.size(), serverSourceFilter.getLoadBalancerRoute(requestConfig).getAvailableServers().size());

        HashMap<Server, AtomicInteger> serverCountMap = new HashMap<>();
        for (int i = 0; i < servers.size() * repeat; i++) {
            LoadBalancerRequestContext requestContext = loadBalancer.getRequestContext(requestConfig);
            if (serverCountMap.containsKey(requestContext.getServer()))
                serverCountMap.get(requestContext.getServer()).incrementAndGet();
            else
                serverCountMap.put(requestContext.getServer(), new AtomicInteger(1));
        }

        for (AtomicInteger atomicInteger : serverCountMap.values()) {
            Assert.assertEquals(2, atomicInteger.intValue());
        }


        List<LoadBalancerRoute> newRoutes = new ArrayList<>();
        List<Server> newServers = Lists.newArrayList(servers.get(0));
        newRoutes.add(new LoadBalancerRoute(lbId, Lists.newArrayList(new ServerGroup("default", 1, servers, null))));
        newRoutes.add(new LoadBalancerRoute("newRoute", Lists.newArrayList(new ServerGroup("default", 1, newServers, null))));
        serverSource.setLoadBalanceRoutes(newRoutes);

        Assert.assertEquals(servers.size(), serverSourceFilter.getLoadBalancerRoute(requestConfig).getServers().size());
        Assert.assertEquals(servers.size(), serverSourceFilter.getLoadBalancerRoute(requestConfig).getAvailableServers().size());

        serverCountMap.clear();
        for (int i = 0; i < servers.size() * repeat; i++) {
            LoadBalancerRequestContext requestContext = loadBalancer.getRequestContext(requestConfig);
            if (serverCountMap.containsKey(requestContext.getServer()))
                serverCountMap.get(requestContext.getServer()).incrementAndGet();
            else
                serverCountMap.put(requestContext.getServer(), new AtomicInteger(1));
        }

        for (AtomicInteger atomicInteger : serverCountMap.values()) {
            Assert.assertEquals(2, atomicInteger.intValue());
        }
    }
    
    @Test
    public void FilterInvalidEntitiesTest1() throws IOException {
        String lbId = Thread.currentThread().getStackTrace()[1].getMethodName();
        String backupFileName = "/opt/data/" + managerId + "." + lbId;
    
        File file = new File(backupFileName);
        file.delete();
    
        List<Server> servers = new ArrayList<>();
        
        LoadBalancerManager factory = LoadBalancerManager.getManager(managerId, TestUtils.getLoadBalancerManagerConfig());
        DefaultDynamicServerSource serverSource = DefaultDynamicServerSource.fromServers(lbId, servers);
        LoadBalancerConfig config = new LoadBalancerConfig.Builder().setServerSource(serverSource).build();
        final LoadBalancer loadBalancer = factory.getLoadBalancer(lbId, config);
        DefaultLoadBalancer defaultLoadBalancer = (DefaultLoadBalancer)loadBalancer;
        
        ServerSourceManager manager = defaultLoadBalancer.getLoadBalancerContext().serverSourceManager();
        
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            String data = "[{\"routeId\":\"default-route-rule\",\"serverGroups\":[{\"groupId\":\"framework.soa.testservice.v1.testservice/sha/ntgxh/921809\",\"weight\":5,\"servers\":[{\"serverId\":\"10.2.4.134:10231\",\"metadata\":{\"subenv\":\"fat8023\",\"appid\":null,\"healthCheckUrl\":\"http://10.2.4.134:10231/test-service/checkhealth.json\",\"url\":\"http://10.2.4.134:10231/test-service/\"},\"alive\":true},{\"serverId\":\"10.2.4.133:10231\",\"metadata\":{\"subenv\":\"fat8023\",\"appid\":null,\"healthCheckUrl\":\"http://10.2.4.133:10231/test-service/checkhealth.json\",\"url\":\"http://10.2.4.133:10231/test-service/\"},\"alive\":true}],\"metadata\":{}}]}]";
            outputStream.write(data.getBytes());
            
            List<LoadBalancerRoute> result = manager.restore();
            Map<String, String> tags = defaultLoadBalancer.getLoadBalancerContext().additionalInfo();
            result = LoadBalancerRoutes.filterInvalidEntities(result, _logger, tags);
            Assert.assertEquals(1, result.size());
            Assert.assertEquals(1, result.get(0).getServerGroups().size());
            Assert.assertEquals(2, result.get(0).getServerGroups().get(0).getServers().size());
        }
        finally {
            file.delete();
        }
    }

    @Test
    public void FilterInvalidEntitiesTest2() throws IOException {
        String lbId = Thread.currentThread().getStackTrace()[1].getMethodName();
        String backupFileName = "/opt/data/" + managerId + "." + lbId;
    
        File file = new File(backupFileName);
        file.delete();
    
        List<Server> servers = new ArrayList<>();
    
        LoadBalancerManager factory = LoadBalancerManager.getManager(managerId, TestUtils.getLoadBalancerManagerConfig());
        DefaultDynamicServerSource serverSource = DefaultDynamicServerSource.fromServers(lbId, servers);
        LoadBalancerConfig config = new LoadBalancerConfig.Builder().setServerSource(serverSource).build();
        final LoadBalancer loadBalancer = factory.getLoadBalancer(lbId, config);
        DefaultLoadBalancer defaultLoadBalancer = (DefaultLoadBalancer)loadBalancer;
    
        ServerSourceManager manager = defaultLoadBalancer.getLoadBalancerContext().serverSourceManager();
        
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            String data = "[{\"routeId\":\"\",\"serverGroups\":[{\"groupId\":\"framework.soa.testservice.v1.testservice/sha/ntgxh/921809\",\"weight\":5,\"servers\":[{\"serverId\":\"10.2.4.134:10231\",\"metadata\":{\"subenv\":\"fat8023\",\"appid\":null,\"healthCheckUrl\":\"http://10.2.4.134:10231/test-service/checkhealth.json\",\"url\":\"http://10.2.4.134:10231/test-service/\"},\"alive\":true},{\"serverId\":\"10.2.4.133:10231\",\"metadata\":{\"subenv\":\"fat8023\",\"appid\":null,\"healthCheckUrl\":\"http://10.2.4.133:10231/test-service/checkhealth.json\",\"url\":\"http://10.2.4.133:10231/test-service/\"},\"alive\":true}],\"metadata\":{}}]}]";
            outputStream.write(data.getBytes());
        
            List<LoadBalancerRoute> result = manager.restore();
            Map<String, String> tags = defaultLoadBalancer.getLoadBalancerContext().additionalInfo();
            result = LoadBalancerRoutes.filterInvalidEntities(result, _logger, tags);
            Assert.assertEquals(0, result.size());
        }
        finally {
            file.delete();
        }
    }
    
    @Test
    public void FilterInvalidEntitiesTest3() throws IOException {
        String lbId = Thread.currentThread().getStackTrace()[1].getMethodName();
        String backupFileName = "/opt/data/" + managerId + "." + lbId;
    
        File file = new File(backupFileName);
        file.delete();
    
        List<Server> servers = new ArrayList<>();
    
        LoadBalancerManager factory = LoadBalancerManager.getManager(managerId, TestUtils.getLoadBalancerManagerConfig());
        DefaultDynamicServerSource serverSource = DefaultDynamicServerSource.fromServers(lbId, servers);
        LoadBalancerConfig config = new LoadBalancerConfig.Builder().setServerSource(serverSource).build();
        final LoadBalancer loadBalancer = factory.getLoadBalancer(lbId, config);
        DefaultLoadBalancer defaultLoadBalancer = (DefaultLoadBalancer)loadBalancer;
    
        ServerSourceManager manager = defaultLoadBalancer.getLoadBalancerContext().serverSourceManager();
    
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            String data = "[{\"routeId\":\"default-route-rule\",\"serverGroups\":[{\"groupId\":\"\",\"weight\":5,\"servers\":[{\"serverId\":\"10.2.4.134:10231\",\"metadata\":{\"subenv\":\"fat8023\",\"appid\":null,\"healthCheckUrl\":\"http://10.2.4.134:10231/test-service/checkhealth.json\",\"url\":\"http://10.2.4.134:10231/test-service/\"},\"alive\":true},{\"serverId\":\"10.2.4.133:10231\",\"metadata\":{\"subenv\":\"fat8023\",\"appid\":null,\"healthCheckUrl\":\"http://10.2.4.133:10231/test-service/checkhealth.json\",\"url\":\"http://10.2.4.133:10231/test-service/\"},\"alive\":true}],\"metadata\":{}}]}]";
            outputStream.write(data.getBytes());
        
            List<LoadBalancerRoute> result = manager.restore();
            Map<String, String> tags = defaultLoadBalancer.getLoadBalancerContext().additionalInfo();
            result = LoadBalancerRoutes.filterInvalidEntities(result, _logger, tags);
            Assert.assertEquals(1, result.size());
            Assert.assertEquals(0, result.get(0).getServerGroups().size());
        }
        finally {
            file.delete();
        }
    }
    
    @Test
    public void FilterInvalidEntitiesTest4() throws IOException {
        String lbId = Thread.currentThread().getStackTrace()[1].getMethodName();
        String backupFileName = "/opt/data/" + managerId + "." + lbId;
    
        File file = new File(backupFileName);
        file.delete();
    
        List<Server> servers = new ArrayList<>();
    
        LoadBalancerManager factory = LoadBalancerManager.getManager(managerId, TestUtils.getLoadBalancerManagerConfig());
        DefaultDynamicServerSource serverSource = DefaultDynamicServerSource.fromServers(lbId, servers);
        LoadBalancerConfig config = new LoadBalancerConfig.Builder().setServerSource(serverSource).build();
        final LoadBalancer loadBalancer = factory.getLoadBalancer(lbId, config);
        DefaultLoadBalancer defaultLoadBalancer = (DefaultLoadBalancer)loadBalancer;
    
        ServerSourceManager manager = defaultLoadBalancer.getLoadBalancerContext().serverSourceManager();
        
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            String data = "[{\"routeId\":\"default-route-rule\",\"serverGroups\":[{\"groupId\":\"framework.soa.testservice.v1.testservice/sha/ntgxh/921809\",\"weight\":5,\"servers\":[{\"serverId\":\"10.2.4.134:10231\",\"metadata\":{\"subenv\":\"fat8023\",\"appid\":null,\"healthCheckUrl\":\"http://10.2.4.134:10231/test-service/checkhealth.json\",\"url\":\"http://10.2.4.134:10231/test-service/\"},\"alive\":true},{\"serverId\":\"\",\"metadata\":{\"subenv\":\"fat8023\",\"appid\":null,\"healthCheckUrl\":\"http://10.2.4.133:10231/test-service/checkhealth.json\",\"url\":\"http://10.2.4.133:10231/test-service/\"},\"alive\":true}],\"metadata\":{}}]}]";
            outputStream.write(data.getBytes());
        
            List<LoadBalancerRoute> result = manager.restore();
            Map<String, String> tags = defaultLoadBalancer.getLoadBalancerContext().additionalInfo();
            result = LoadBalancerRoutes.filterInvalidEntities(result, _logger, tags);
            Assert.assertEquals(1, result.size());
            Assert.assertEquals(1, result.get(0).getServerGroups().size());
            Assert.assertEquals(1, result.get(0).getServerGroups().get(0).getServers().size());
        }
        finally {
            file.delete();
        }
    }
}
