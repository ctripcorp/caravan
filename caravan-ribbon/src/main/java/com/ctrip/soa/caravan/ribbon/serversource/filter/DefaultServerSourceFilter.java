package com.ctrip.soa.caravan.ribbon.serversource.filter;

import com.ctrip.soa.caravan.common.delegate.Func;
import com.ctrip.soa.caravan.common.metric.StatusMetric;
import com.ctrip.soa.caravan.common.metric.StatusMetricConfig;
import com.ctrip.soa.caravan.common.metric.StatusMetricManager;
import com.ctrip.soa.caravan.common.metric.StatusProvider;
import com.ctrip.soa.caravan.common.value.CollectionValues;
import com.ctrip.soa.caravan.common.value.MapValues;
import com.ctrip.soa.caravan.common.value.StringValues;
import com.ctrip.soa.caravan.ribbon.*;
import com.ctrip.soa.caravan.ribbon.loadbalancer.LoadBalancerContext;
import com.ctrip.soa.caravan.ribbon.util.LogUtil;
import com.ctrip.soa.caravan.ribbon.util.PingUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.ctrip.soa.caravan.common.value.ConcurrentHashMapValues.getOrAddWithLock;
import static com.ctrip.soa.caravan.configuration.util.PropertyKeyGenerator.generateKey;
import static com.ctrip.soa.caravan.ribbon.util.LoadBalancerRoutes.filterInvalidEntities;

/**
 * Created by w.jian on 2016/7/26.
 */
public class DefaultServerSourceFilter implements ServerSourceFilter {

    private static Logger _logger = LoggerFactory.getLogger(DefaultServerSourceFilter.class);
    private final LoadBalancerContext _loadBalancerContext;

    private volatile boolean _initialized;
    private volatile List<LoadBalancerRoute> _routes = new ArrayList<>();
    private volatile HashMap<String, Server> _serverMap = new HashMap<>();
    private volatile HashMap<String, LoadBalancerRoute> _routeMap = new HashMap<>();

    private StatusMetricManager<Double> _statusMetricManager;
    private ConcurrentHashMap<String, StatusMetric<Double>> serverCountMetrics;
    private ConcurrentHashMap<String, StatusMetric<Double>> availableServerCountMetrics;

    private List<ServerContextChangeListener> _listeners = new CopyOnWriteArrayList<>();
    private List<SeekRouteListener> _seekRouteListeners = new CopyOnWriteArrayList<>();

    public DefaultServerSourceFilter(LoadBalancerContext loadBalancerContext) {
        _loadBalancerContext = loadBalancerContext;

        _statusMetricManager = loadBalancerContext.statusMetricManager();
        serverCountMetrics = new ConcurrentHashMap<>();
        availableServerCountMetrics = new ConcurrentHashMap<>();
    }
    
    @Override
    public List<LoadBalancerRoute> getLoadBalancerRoutes() {
        if (_routes == null)
            _routes = new ArrayList<>();
        return _routes;
    }

    @Override
    public synchronized void setLoadBalancerRoutes(List<LoadBalancerRoute> newRoutes) {
        if (CollectionValues.isNullOrEmpty(newRoutes)) {
            LogUtil.info(_logger, "Ignored null or empty routes", _loadBalancerContext.additionalInfo());
            return;
        }

        newRoutes = filterInvalidEntities(newRoutes, _logger, _loadBalancerContext.additionalInfo());
        
        HashMap<String, Server> newServerMap = new HashMap<>();
        PingUtil pingUtil = new PingUtil(_loadBalancerContext, _logger);
        for (LoadBalancerRoute newRoute : newRoutes) {
            int serverCount = newRoute.getServers().size();
            boolean needPing = _initialized && serverCount > _loadBalancerContext.getMinAvailableServerCount();
            for (Server newServer : newRoute.getServers()) {
                Server server = _serverMap.get(newServer.getServerId());
                if (server != null)
                    newServer.setAlive(server.isAlive());
                else if (needPing && pingUtil.hasPing())
                    newServer.setAlive(pingUtil.isAlive(newServer));
                newServerMap.put(newServer.getServerId(), newServer);
            }
        }

        HashMap<String, LoadBalancerRoute> newRouteMap = buildRouteMaps(newRoutes);
        if (MapValues.isNullOrEmpty(newRouteMap))
            return;

        String message = String.format("Routes changed from\n%s\nto\n%s", _routes, newRoutes);
        LogUtil.info(_logger, message, _loadBalancerContext.additionalInfo());
        initMetric(newRoutes);

        _routes = newRoutes;
        _routeMap = newRouteMap;
        _serverMap = newServerMap;
        _initialized = true;

        refresh();
        raiseServerContextChangeEvent();
    }

    private HashMap<String, LoadBalancerRoute> buildRouteMaps(List<LoadBalancerRoute> newRoutes) {
        HashMap<String, LoadBalancerRoute> newRouteMap = new HashMap<>();
        for (LoadBalancerRoute newRoute : newRoutes) {
            if (newRoute.getServers().size() == 0) {
                String message = "Ignore empty route:" + newRoute.getRouteId();
                LogUtil.info(_logger, message, _loadBalancerContext.additionalInfo());
                continue;
            }
            
            newRouteMap.put(newRoute.getRouteId(), newRoute);
            if (!_routeMap.containsKey(newRoute.getRouteId())) {
                LogUtil.info(_logger, "Add new route: " + newRoute.getRouteId(), _loadBalancerContext.additionalInfo());
            } else {
                LogUtil.info(_logger, "Update route: " + newRoute.getRouteId(), _loadBalancerContext.additionalInfo());
            }
        }
        return newRouteMap;
    }

    @Override
    public LoadBalancerRoute getLoadBalancerRoute(LoadBalancerRequestConfig loadBalancerRequestConfig) {
        if (loadBalancerRequestConfig == null || CollectionValues.isNullOrEmpty(loadBalancerRequestConfig.getRouteChains()))
            return null;

        HashMap<String, LoadBalancerRoute> routeMap = _routeMap;
        TreeSet<LoadBalancerRouteConfig> routeChains = loadBalancerRequestConfig.getRouteChains();
        LoadBalancerRouteConfig loadBalancerRouteConfig = routeChains.first();
        while (loadBalancerRouteConfig != null) {
            String routeId = loadBalancerRouteConfig.getRouteId();
            if (!StringValues.isNullOrWhitespace(routeId)) {
                LoadBalancerRoute route = routeMap.get(routeId);
                if (route != null) {
                    SeekRouteEvent event = new DefaultSeekRouteEvent(route);
                    raiseSeekRouteEvent(event);
                    return event.getRoute();
                }
            }
            if (!loadBalancerRouteConfig.isAllowFallback())
                return null;

            loadBalancerRouteConfig = routeChains.higher(loadBalancerRouteConfig);
        }
        return null;
    }

    @Override
    public synchronized void refresh() {
        List<LoadBalancerRoute> routes = _routes;
        for (LoadBalancerRoute route : routes) {
            for (ServerGroup serverGroup : route.getServerGroups()) {
                serverGroup.refreshAvailableServers();
            }
        }
    }

    @Override
    public void addServerContextChangeListener(ServerContextChangeListener listener) {
        if (listener == null)
            return;
        
        _listeners.add(listener);
    }
    
    @Override
    public void addSeekRouteListener(SeekRouteListener listener) {
        if (listener == null)
            return;
        
        _seekRouteListeners.add(listener);
    }
    
    private void raiseServerContextChangeEvent() {
        ServerContextChangeEvent event = new ServerContextChangeEvent();
        for (ServerContextChangeListener listener : _listeners) {
            try {
                listener.onChange(event);
            } catch (Throwable t) {
                String message = "Error occurred while raising ServerContextChangeEvent.";
                LogUtil.warn(_logger, message, t, _loadBalancerContext.additionalInfo());
            }
        }
    }
    
    private void raiseSeekRouteEvent(SeekRouteEvent event) {
        for (SeekRouteListener listener : _seekRouteListeners) {
            try {
                listener.onSeekRoute(event);
            } catch (Throwable t) {
                String message = "Error occurred while raising SeekRouteEvent.";
                LogUtil.warn(_logger, message, t, _loadBalancerContext.additionalInfo());
            }
        }
    }

    private void initMetric(List<LoadBalancerRoute> routes) {
        for (LoadBalancerRoute route : routes) {
            if (StringValues.isNullOrWhitespace(route.getRouteId())) {
                continue;
            }
            
            initServerCountMetric(route);
            initAvailableServerCountMetric(route);
        }
    }

    private void initServerCountMetric(final LoadBalancerRoute route) {
        getOrAddWithLock(serverCountMetrics, route.getRouteId(), new Func<StatusMetric<Double>>() {
            @Override
            public StatusMetric<Double> execute() {
                String managerId = _loadBalancerContext.managerId();
                String loadBalancerId = _loadBalancerContext.loadBalancerKey();

                String metricId = generateKey(loadBalancerId, route.getRouteId(), "ribbon.servers.count");
                String metricName = generateKey(managerId, "ribbon.servers.count");
                HashMap<String, String> metadata = new HashMap<>();
                metadata.put("metric_name_audit", metricName);
                metadata.put("loadbalancerid", _loadBalancerContext.loadBalancerKey());
                metadata.put("routeid", route.getRouteId());
                StatusProvider<Double> statusProvider = new StatusProvider<Double>() {
                    @Override
                    public Double getStatus() {
                        return Double.valueOf(route.getServers().size());
                    }
                };
                return _statusMetricManager.getMetric(metricId, new StatusMetricConfig<>(statusProvider, metadata));
            }
        });
    }

    private void initAvailableServerCountMetric(final LoadBalancerRoute route) {
        getOrAddWithLock(availableServerCountMetrics, route.getRouteId(), new Func<StatusMetric<Double>>() {
            @Override
            public StatusMetric<Double> execute() {
                String managerId = _loadBalancerContext.managerId();
                String loadBalancerId = _loadBalancerContext.loadBalancerKey();

                String metricId = generateKey(loadBalancerId, route.getRouteId(), "ribbon.servers.available.count");
                String metricName = generateKey(managerId, "ribbon.servers.available.count");
                HashMap<String, String> metadata = new HashMap<>();
                metadata.put("metric_name_audit", metricName);
                metadata.put("loadbalancerid", _loadBalancerContext.loadBalancerKey());
                metadata.put("routeid", route.getRouteId());
                StatusProvider<Double> statusProvider = new StatusProvider<Double>() {
                    @Override
                    public Double getStatus() {
                        return Double.valueOf(route.getAvailableServers().size());
                    }
                };
                return _statusMetricManager.getMetric(metricId, new StatusMetricConfig<>(statusProvider, metadata));
            }
        });

    }
}
