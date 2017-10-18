package com.ctrip.soa.caravan.ribbon.rule;

import com.ctrip.soa.caravan.ribbon.*;
import com.ctrip.soa.caravan.ribbon.serversource.DefaultDynamicServerSource;
import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by w.jian on 2017/2/27.
 */
public class WeightedRoundRobinRuleTest {

    @Test
    public void weightedRoundRobinTest() {
        final int serverCount = 10;
        final int repeatTimes = 100;
        final int threadCount = 20;
        int serverSumCounter = 0;
        final String factoryId = "soa";
        final String lbId = Thread.currentThread().getStackTrace()[1].getMethodName();
        final LoadBalancerRequestConfig loadBalancerRequestConfig = new LoadBalancerRequestConfig(lbId);

        final HashMap<Integer, AtomicInteger> indexCountMapping = new HashMap<>();
        List<ServerGroup> serverGroups = new ArrayList<>();
        for (int i = 0; i < serverCount; i++) {
            HashMap<String, String> metadata = new HashMap<>();
            metadata.put("Index", Integer.toString(i));
            Server server = Server.newBuilder().setServerId("Server_" + i).setMetadata(metadata).build();
            ServerGroup serverGroup = ServerGroup.newBuilder()
                    .setGroupId(server.getServerId())
                    .setWeight(i)
                    .setServers(Lists.newArrayList(server))
                    .build();
            serverGroups.add(serverGroup);
            indexCountMapping.put(i, new AtomicInteger(0));
            serverSumCounter += i;
        }

        final int serverSumCount = serverSumCounter;
        Ping ping = new Ping() {
            @Override
            public boolean isAlive(Server server) {
                return true;
            }
        };
        LoadBalancerManager factory = LoadBalancerManager.getManager(factoryId, TestUtils.getLoadBalancerManagerConfig());
        DefaultDynamicServerSource serverSource = DefaultDynamicServerSource.fromServerGroups(lbId, serverGroups);
        LoadBalancerConfig config = new LoadBalancerConfig.Builder().setPing(ping).setServerSource(serverSource).build();
        final LoadBalancer loadBalancer = factory.getLoadBalancer(lbId, config);

        Runnable action = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < repeatTimes; i++) {
                    for (int j = 0; j < serverSumCount; j++) {
                        LoadBalancerRequestContext requestContext = loadBalancer.getRequestContext(loadBalancerRequestConfig);
                        int index = Integer.parseInt(requestContext.getServer().getMetadata().get("Index"));
                        indexCountMapping.get(index).incrementAndGet();
                    }
                }
            }
        };
        Thread[] threads = new Thread[threadCount];
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(action);
            threads[i].start();

        }
        Threads.wait(threads, 100);

        for (Map.Entry<Integer, AtomicInteger> item : indexCountMapping.entrySet()) {
            Assert.assertEquals(item.getKey() * repeatTimes * threadCount, item.getValue().intValue());
        }
    }

    @Test
    public void weightedRoundRobinWithUnavailableServerTest() {
        final int serverCount = 10;
        final int repeatTimes = 100;
        final int threadCount = 20;
        int serverSumCounter = 0;
        final String factoryId = "soa";
        final String lbId = Thread.currentThread().getStackTrace()[1].getMethodName();
        final LoadBalancerRequestConfig loadBalancerRequestConfig = new LoadBalancerRequestConfig(lbId);

        final HashMap<Integer, AtomicInteger> indexCountMapping = new HashMap<>();
        List<Server> servers = new ArrayList<>();
        List<ServerGroup> serverGroups = new ArrayList<>();
        for (int i = 0; i < serverCount; i++) {
            HashMap<String, String> metadata = new HashMap<>();
            metadata.put("Index", Integer.toString(i));
            Server server = Server.newBuilder()
                    .setServerId("Server_" + i)
                    .setIsAlive(i % 2 == 0)
                    .setMetadata(metadata)
                    .build();
            servers.add(server);
            ServerGroup serverGroup = ServerGroup.newBuilder()
                    .setGroupId(server.getServerId())
                    .setWeight(i)
                    .setServers(Lists.newArrayList(server))
                    .build();
            serverGroups.add(serverGroup);
            indexCountMapping.put(i, new AtomicInteger(0));
            if (server.isAlive())
                serverSumCounter += i;
        }

        final int serverSumCount = serverSumCounter;
        Ping ping = new Ping() {
            @Override
            public boolean isAlive(Server server) {
                int index = Integer.parseInt(server.getMetadata().get("Index"));
                return index % 2 == 0;
            }
        };
        LoadBalancerManager factory = LoadBalancerManager.getManager(factoryId, TestUtils.getLoadBalancerManagerConfig());
        DefaultDynamicServerSource serverSource = DefaultDynamicServerSource.fromServerGroups(lbId, serverGroups);
        LoadBalancerConfig config = new LoadBalancerConfig.Builder().setPing(ping).setServerSource(serverSource).build();
        final LoadBalancer loadBalancer = factory.getLoadBalancer(lbId, config);

        Runnable action = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < repeatTimes; i++) {
                    for (int j = 0; j < serverSumCount; j++) {
                        LoadBalancerRequestContext requestContext = loadBalancer.getRequestContext(loadBalancerRequestConfig);
                        int index = Integer.parseInt(requestContext.getServer().getMetadata().get("Index"));
                        indexCountMapping.get(index).incrementAndGet();
                    }
                }
            }
        };
        Thread[] threads = new Thread[threadCount];
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(action);
            threads[i].start();
        }
        Threads.wait(threads, 100);

        for (Map.Entry<Integer, AtomicInteger> item : indexCountMapping.entrySet()) {
            Server server = servers.get(item.getKey());
            if (ping.isAlive(server))
                Assert.assertEquals(server.toString(), item.getKey() * repeatTimes * threadCount, item.getValue().intValue());
            else
                Assert.assertEquals(server.toString(), 0, item.getValue().intValue());
        }
    }

    @Test
    public void weightedRoundRobinWithMultiRouteTest() {
        final int serverCount = 10;
        final int repeatTimes = 100;
        final int threadCount = 20;
        int serverSumCounter = 0;
        final String factoryId = "soa";
        final String lbId = Thread.currentThread().getStackTrace()[1].getMethodName();
        final LoadBalancerRequestConfig loadBalancerRequestConfig = new LoadBalancerRequestConfig(lbId);

        final HashMap<Integer, AtomicInteger> indexCountMapping = new HashMap<>();

        List<Server> servers = new ArrayList<>();
        List<ServerGroup> serverGroups = new ArrayList<>();
        for (int i = 0; i < serverCount; i++) {
            HashMap<String, String> metadata = new HashMap<>();
            metadata.put("Index", Integer.toString(i));
            Server server = Server.newBuilder().setServerId("Server_" + i).setMetadata(metadata).build();
            servers.add(server);
            ServerGroup serverGroup = ServerGroup.newBuilder()
                    .setGroupId(server.getServerId())
                    .setWeight(i)
                    .setServers(Lists.newArrayList(server))
                    .build();
            serverGroups.add(serverGroup);
            indexCountMapping.put(i, new AtomicInteger(0));
            serverSumCounter += i;
        }
        LoadBalancerRoute route0 = LoadBalancerRoute.newBuilder().setRouteId(lbId).setServerGroups(serverGroups).build();

        ServerGroup route1Group0 = ServerGroup.newBuilder()
                .setGroupId("server0")
                .setServers(Lists.newArrayList(servers.get(0)))
                .setWeight(2)
                .build();
        ServerGroup route1Group1 = ServerGroup.newBuilder()
                .setGroupId("server1")
                .setServers(Lists.newArrayList(servers.get(1)))
                .setWeight(1)
                .build();
        LoadBalancerRoute route1 = LoadBalancerRoute.newBuilder().setRouteId("route1").setServerGroups(Lists.newArrayList(route1Group0, route1Group1)).build();

        ServerGroup route2Group0 = ServerGroup.newBuilder()
                .setGroupId("server0")
                .setServers(Lists.newArrayList(servers.get(0)))
                .setWeight(2)
                .build();
        ServerGroup route2Group1 = ServerGroup.newBuilder()
                .setGroupId("server1")
                .setServers(Lists.newArrayList(servers.get(1)))
                .setWeight(3)
                .build();
        LoadBalancerRoute route2 = LoadBalancerRoute.newBuilder().setRouteId("route2").setServerGroups(Lists.newArrayList(route2Group0, route2Group1)).build();
        List<LoadBalancerRoute> routes = Lists.newArrayList(route0, route1, route2);

        final LoadBalancerRequestConfig route1LoadBalancerRequestConfig = new LoadBalancerRequestConfig("route1");
        final LoadBalancerRequestConfig route2LoadBalancerRequestConfig = new LoadBalancerRequestConfig("route2");
        final int route1ServerCount = route1Group0.getWeight() + route2Group0.getWeight();
        final int route2ServerCount = route1Group1.getWeight() + route2Group1.getWeight();
        final int route1RequestCount = route1Group0.getWeight() + route1Group1.getWeight();
        final int route2RequestCount = route2Group0.getWeight() + route2Group1.getWeight();

        Ping ping = new Ping() {
            @Override
            public boolean isAlive(Server server) {
                return true;
            }
        };
        LoadBalancerManager factory = LoadBalancerManager.getManager(factoryId, TestUtils.getLoadBalancerManagerConfig());
        DefaultDynamicServerSource serverSource = DefaultDynamicServerSource.fromRoutes(routes);
        LoadBalancerConfig config = new LoadBalancerConfig.Builder().setPing(ping).setServerSource(serverSource).build();
        final LoadBalancer loadBalancer = factory.getLoadBalancer(lbId, config);

        final int serverSumCount = serverSumCounter;
        Runnable action = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < repeatTimes; i++) {
                    for (int j = 0; j < serverSumCount; j++) {
                        LoadBalancerRequestContext requestContext = loadBalancer.getRequestContext(loadBalancerRequestConfig);
                        int index = Integer.parseInt(requestContext.getServer().getMetadata().get("Index"));
                        indexCountMapping.get(index).incrementAndGet();
                    }
                    for (int j = 0; j < route1RequestCount; j++) {
                        LoadBalancerRequestContext requestContext = loadBalancer.getRequestContext(route1LoadBalancerRequestConfig);
                        int index = Integer.parseInt(requestContext.getServer().getMetadata().get("Index"));
                        indexCountMapping.get(index).incrementAndGet();
                    }
                    for (int j = 0; j < route2RequestCount; j++) {
                        LoadBalancerRequestContext requestContext = loadBalancer.getRequestContext(route2LoadBalancerRequestConfig);
                        int index = Integer.parseInt(requestContext.getServer().getMetadata().get("Index"));
                        indexCountMapping.get(index).incrementAndGet();
                    }
                }
            }
        };
        Thread[] threads = new Thread[threadCount];
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(action);
            threads[i].start();
        }
        Threads.wait(threads, 100);

        for (Map.Entry<Integer, AtomicInteger> item : indexCountMapping.entrySet()) {
            if (item.getKey() == 0) {
                Assert.assertEquals(item.getKey().toString(), (item.getKey() + route1ServerCount) * repeatTimes * threadCount, item.getValue().intValue());
            } else if (item.getKey() == 1) {
                Assert.assertEquals(item.getKey().toString(), (item.getKey() + route2ServerCount) * repeatTimes * threadCount, item.getValue().intValue());
            } else {
                Assert.assertEquals(item.getKey().toString(), item.getKey() * repeatTimes * threadCount, item.getValue().intValue());
            }
        }
    }
}
