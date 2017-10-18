package com.ctrip.soa.caravan.ribbon.loadbalancer;

import com.ctrip.soa.caravan.ribbon.*;
import com.ctrip.soa.caravan.ribbon.Server;
import com.ctrip.soa.caravan.ribbon.serversource.DefaultDynamicServerSource;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by w.jian on 2016/6/23.
 */
public class DefaultLoadBalancerTest {

    @Test
    @Ignore
    public void loadBalancerTest () {
        final int serverCount = 10;
        final int threadCount = 4;
        final int repeatTimes = Integer.MAX_VALUE / (serverCount * threadCount) - (serverCount * threadCount);
        final String lbId = Thread.currentThread().getStackTrace()[1].getMethodName();
        Assert.assertTrue(serverCount * repeatTimes * threadCount > Integer.MAX_VALUE / 2);

        final List<Server> servers = new ArrayList<>();
        for (int i = 0; i < serverCount; i++) {
            servers.add(new Server.Builder().setServerId("server_" + i).build());
        }

        final HashMap<Server, AtomicInteger> dataMap = new HashMap<>();
        for (Server server : servers) {
            dataMap.put(server, new AtomicInteger(0));
        }

        final DefaultDynamicServerSource serverSource = new DefaultDynamicServerSource();
        LoadBalancerManager factory = LoadBalancerManager.getManager("soa", TestUtils.getLoadBalancerManagerConfig());
        LoadBalancerConfig config = new LoadBalancerConfig.Builder().setServerSource(serverSource).build();
        final LoadBalancer loadBalancer = factory.getLoadBalancer(lbId, config);
        serverSource.setServers(lbId, servers);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < serverCount * repeatTimes; i++) {
                    LoadBalancerRequestContext context = loadBalancer.getRequestContext(null);
                    dataMap.get(context.getServer()).incrementAndGet();
                }
            }
        };

        Thread[] threads = new Thread[threadCount];
        for (int i = 0; i < threadCount; i++) {
            Thread thread = new Thread(runnable);
            thread.start();
            threads[i] = thread;
        }

        Threads.wait(threads, 10);

        int average = threadCount * repeatTimes;
        int averageDelta = 10;
        for (int i = 0; i < serverCount; i++) {
            Server server = servers.get(i);
            int actual = dataMap.get(server).get();
            System.out.println(server.toString() + " -> " + actual + ", " + average);
        }

        for (int i = 0; i < serverCount; i++) {
            int actual = dataMap.get(servers.get(i)).get();
            Assert.assertTrue(servers.get(i).toString(), Math.abs(actual - average) < averageDelta);
        }
    }
}
