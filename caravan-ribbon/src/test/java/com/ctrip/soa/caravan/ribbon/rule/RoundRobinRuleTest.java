package com.ctrip.soa.caravan.ribbon.rule;

import com.ctrip.soa.caravan.common.delegate.Func;
import com.ctrip.soa.caravan.ribbon.*;
import com.ctrip.soa.caravan.ribbon.Server;
import com.ctrip.soa.caravan.ribbon.serversource.DefaultDynamicServerSource;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by w.jian on 2016/6/21.
 */
public class RoundRobinRuleTest {

    @Test
    public void roundRobinRuleTest() {

        final int serverCount = 10;
        final int repeatTimes = 5;
        final int threadCount = 10;
        final String factoryId = "soa";
        final String lbId = Thread.currentThread().getStackTrace()[1].getMethodName();
        final LoadBalancerRequestConfig loadBalancerRequestConfig = new LoadBalancerRequestConfig(lbId);

        final List<Server> servers = new ArrayList<>();
        final HashMap<Integer, AtomicInteger> indexCountMapping = new HashMap<>();
        for (int i = 0; i < serverCount; i++) {
            HashMap<String, String> metadata = new HashMap<>();
            metadata.put("Index", Integer.toString(i));
            Server server = Server.newBuilder().setServerId("Server_" + i).setMetadata(metadata).build();
            servers.add(server);
            indexCountMapping.put(i, new AtomicInteger(0));
        }

        Ping ping = new Ping() {
            @Override
            public boolean isAlive(Server server) {
                return true;
            }
        };
        LoadBalancerManager factory = LoadBalancerManager.getManager(factoryId, TestUtils.getLoadBalancerManagerConfig());
        DefaultDynamicServerSource serverSource = DefaultDynamicServerSource.fromServers(lbId, servers);
        LoadBalancerConfig config = LoadBalancerConfig.newBuilder()
                .setPing(ping)
                .setRuleFactory(new Func<LoadBalancerRule>() {
                    @Override
                    public LoadBalancerRule execute() {
                        return new RoundRobinRule();
                    }
                })
                .setServerSource(serverSource)
                .build();
        final LoadBalancer loadBalancer = factory.getLoadBalancer(lbId, config);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < serverCount * repeatTimes; i++) {
                    LoadBalancerRequestContext requestContext = loadBalancer.getRequestContext(loadBalancerRequestConfig);
                    int index = Integer.parseInt(requestContext.getServer().getMetadata().get("Index"));
                    indexCountMapping.get(index).incrementAndGet();
                }
            }
        };

        Thread[] threads = new Thread[threadCount];
        for (int i = 0; i < threadCount; i++) {
            Thread thread = new Thread(runnable);
            thread.start();
            threads[i] = thread;
        }

        Threads.wait(threads, 200);

        int count = threadCount * repeatTimes;
        for (Server server : servers) {
            int index = Integer.parseInt(server.getMetadata().get("Index"));
            Assert.assertEquals(servers.toString(), count, indexCountMapping.get(index).intValue());
        }
    }

    @Test
    public void roundRobinRuleWithUnavailableServerTest() {

        final int serverCount = 10;
        final int repeatTimes = 5;
        final int threadCount = 10;
        final String factoryId = "soa";
        final String lbId = Thread.currentThread().getStackTrace()[1].getMethodName();
        final LoadBalancerRequestConfig loadBalancerRequestConfig = new LoadBalancerRequestConfig(lbId);

        final List<Server> servers = new ArrayList<>();
        final HashMap<Integer, AtomicInteger> indexCountMapping = new HashMap<>();
        for (int i = 0; i < serverCount; i++) {
            HashMap<String, String> metadata = new HashMap<>();
            metadata.put("Index", Integer.toString(i));
            Server server = Server.newBuilder()
                    .setServerId("Server_" + i)
                    .setIsAlive(i % 2 == 0)
                    .setMetadata(metadata)
                    .build();
            servers.add(server);
            indexCountMapping.put(i, new AtomicInteger(0));
        }

        Ping ping = new Ping() {
            @Override
            public boolean isAlive(Server server) {
                int index = Integer.parseInt(server.getMetadata().get("Index"));
                return index % 2 == 0;
            }
        };
        LoadBalancerManager factory = LoadBalancerManager.getManager(factoryId, TestUtils.getLoadBalancerManagerConfig());
        DefaultDynamicServerSource serverSource = DefaultDynamicServerSource.fromServers(lbId, servers);
        LoadBalancerConfig config = new LoadBalancerConfig.Builder()
                .setPing(ping)
                .setRuleFactory(new Func<LoadBalancerRule>() {
                    @Override
                    public LoadBalancerRule execute() {
                        return new RoundRobinRule();
                    }
                })
                .setServerSource(serverSource).build();
        final LoadBalancer loadBalancer = factory.getLoadBalancer(lbId, config);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < serverCount * repeatTimes; i++) {
                    LoadBalancerRequestContext requestContext = loadBalancer.getRequestContext(loadBalancerRequestConfig);
                    int index = Integer.parseInt(requestContext.getServer().getMetadata().get("Index"));
                    indexCountMapping.get(index).incrementAndGet();
                }
            }
        };

        Thread[] threads = new Thread[threadCount];
        for (int i = 0; i < threadCount; i++) {
            Thread thread = new Thread(runnable);
            thread.start();
            threads[i] = thread;
        }

        Threads.wait(threads, 200);

        int count = threadCount * repeatTimes;
        for (Server server : servers) {
            int index = Integer.parseInt(server.getMetadata().get("Index"));
            if (index % 2 == 0) {
                Assert.assertEquals(servers.toString(), count * 2, indexCountMapping.get(index).intValue());
            } else {
                Assert.assertEquals(servers.toString(), 0, indexCountMapping.get(index).intValue());
            }
        }
    }
}
