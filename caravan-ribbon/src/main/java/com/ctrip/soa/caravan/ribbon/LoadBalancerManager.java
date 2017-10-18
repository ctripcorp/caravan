package com.ctrip.soa.caravan.ribbon;

import com.ctrip.soa.caravan.common.concurrent.ThreadFactories;
import com.ctrip.soa.caravan.common.delegate.Func;
import com.ctrip.soa.caravan.common.value.ConcurrentHashMapValues;
import com.ctrip.soa.caravan.common.value.checker.NullArgumentChecker;
import com.ctrip.soa.caravan.common.value.checker.StringArgumentChecker;
import com.ctrip.soa.caravan.configuration.typed.dynamic.TypedDynamicProperty;
import com.ctrip.soa.caravan.configuration.util.RangePropertyConfig;
import com.ctrip.soa.caravan.ribbon.loadbalancer.DefaultLoadBalancerContext;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by w.jian on 2016/7/13.
 */
public class LoadBalancerManager {

    private static ConcurrentHashMap<String, LoadBalancerManager> _managerCache = new ConcurrentHashMap<>();

    private ConcurrentHashMap<String, LoadBalancer> _loadBalancerCache = new ConcurrentHashMap<>();

    private String _managerId;

    private LoadBalancerManagerConfig _loadBalancerManagerConfig;
    
    private static final int DefaultCheckHealthCount = 2;
    private static final int MinCheckHealthCount = 1;
    private static final int MaxCheckHealthCount = 8;
    private TypedDynamicProperty<Integer> _checkHealthThreadCountProperty;
    private ScheduledExecutorService _checkHealthExecutorService;

    private LoadBalancerManager(String managerId, LoadBalancerManagerConfig lbConfig) {
        _managerId = managerId;
        _loadBalancerManagerConfig = lbConfig;
        
        initializeCheckHealthThreadCountProperty(lbConfig);
        initializeCheckHealthExecutorService(_checkHealthThreadCountProperty.typedValue());
    }
    
    private void initializeCheckHealthThreadCountProperty(LoadBalancerManagerConfig lbConfig) {
        String propertyKey = String.format("%s.%s", _managerId, ConfigurationKeys.CheckHealthCount);
        RangePropertyConfig<Integer> propertyConfig = new RangePropertyConfig<>(DefaultCheckHealthCount, MinCheckHealthCount, MaxCheckHealthCount);
        _checkHealthThreadCountProperty = lbConfig.getProperties().getIntProperty(propertyKey, propertyConfig);
    }
    
    private void initializeCheckHealthExecutorService(int checkHealthCount) {
        String nameFormat = "ribbon-healthCheck-%d-" + _managerId;
        _checkHealthExecutorService = Executors.newScheduledThreadPool(checkHealthCount, ThreadFactories.newDaemonThreadFactory(nameFormat));
    }

    public static LoadBalancerManager getManager(final String managerId, final LoadBalancerManagerConfig lbConfig) {
        StringArgumentChecker.DEFAULT.check(managerId, "managerId");
        NullArgumentChecker.DEFAULT.check(lbConfig, "lbConfig");

        return ConcurrentHashMapValues.getOrAddWithLock(_managerCache, managerId, new Func<LoadBalancerManager>(){
            @Override
            public LoadBalancerManager execute() {
                return new LoadBalancerManager(managerId, lbConfig);
            }
        });
    }

    private LoadBalancer newLoadBalancer(String loadBalancerId, LoadBalancerConfig loadBalancerConfig) {
        DefaultLoadBalancerContext lbContext = new DefaultLoadBalancerContext(this, loadBalancerId, loadBalancerConfig, _checkHealthExecutorService);
        return lbContext.loadBalancer();
    }

    public LoadBalancer getLoadBalancer(final String lbId, final LoadBalancerConfig lbConfig) {
        StringArgumentChecker.DEFAULT.check(lbId, "lbId");
        NullArgumentChecker.DEFAULT.check(lbConfig, "lbConfig");

        return ConcurrentHashMapValues.getOrAddWithLock(_loadBalancerCache, lbId, new Func<LoadBalancer>() {
            @Override
            public LoadBalancer execute() {
                return newLoadBalancer(lbId, lbConfig);
            }
        });
    }

    public String getManagerId() {
        return _managerId;
    }

    public LoadBalancerManagerConfig getLoadBalancerManagerConfig() {
        return _loadBalancerManagerConfig;
    }
}
