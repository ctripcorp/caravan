package com.ctrip.soa.caravan.ribbon.serversource.monitor;

import com.ctrip.soa.caravan.common.delegate.Func;
import com.ctrip.soa.caravan.common.metric.AuditMetric;
import com.ctrip.soa.caravan.common.metric.EventMetric;
import com.ctrip.soa.caravan.common.metric.MetricConfig;
import com.ctrip.soa.caravan.common.value.ConcurrentHashMapValues;
import com.ctrip.soa.caravan.common.value.checker.NullArgumentChecker;
import com.ctrip.soa.caravan.configuration.dynamic.PropertyChangeEvent;
import com.ctrip.soa.caravan.configuration.dynamic.PropertyChangeListener;
import com.ctrip.soa.caravan.configuration.facade.TypedDynamicCachedCorrectedProperties;
import com.ctrip.soa.caravan.configuration.typed.dynamic.cached.corrected.TypedDynamicCachedCorrectedProperty;
import com.ctrip.soa.caravan.ribbon.ConfigurationKeys;
import com.ctrip.soa.caravan.ribbon.LoadBalancerRoute;
import com.ctrip.soa.caravan.ribbon.Server;
import com.ctrip.soa.caravan.ribbon.loadbalancer.LoadBalancerContext;
import com.ctrip.soa.caravan.ribbon.server.ServerStats;
import com.ctrip.soa.caravan.ribbon.util.LogUtil;
import com.ctrip.soa.caravan.ribbon.util.PingUtil;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static com.ctrip.soa.caravan.configuration.util.PropertyKeyGenerator.generateKey;
import static com.ctrip.soa.caravan.configuration.util.PropertyValueGetter.getTypedValue;

/**
 * Created by w.jian on 2016/6/15.
 */
@SuppressWarnings("all")
public class DefaultServerSourceMonitor implements ServerSourceMonitor {

    private static Logger _logger = LoggerFactory.getLogger(DefaultServerSourceMonitor.class);

    private static final String CheckHealthLatencyMetricSuffix = "ribbon.checkhealth.latency";
    private static final String CheckHealthEventMetricSuffix = "ribbon.checkhealth.event";
    private static final String AvailabilityKey = "Availability";

    private static final int defaultFailureThresholdPercent = 50;
    private static final int failureThresholdPercentLowerBound = 0;
    private static final int failureThresholdPercentUpperBound = 100;

    private static final int defaultCheckInterval = 2000;
    private static final int checkIntervalLowerBound = 1000;
    private static final int checkIntervalUpperBound = 10000;

    private volatile boolean _lastHasUnavailableServers;
    private ScheduledFuture<?> _checkHealthFuture;
    private int _interval;
    private LoadBalancerContext _loadBalancerContext;
    private Map<String, String> _additionInfo;
    private PingUtil _pingUtil;

    private ConcurrentHashMap<String, AuditMetric> _checkHealthLatencyMetrics;
    private ConcurrentHashMap<String, EventMetric> _checkHealthEventMetrics;

    private TypedDynamicCachedCorrectedProperty<Integer> _managerLevelFailureThresholdPercentProperty;
    private TypedDynamicCachedCorrectedProperty<Integer> _managerLevelCheckIntervalProperty;
    private TypedDynamicCachedCorrectedProperty<Integer> _failureThresholdPercentProperty;
    private TypedDynamicCachedCorrectedProperty<Integer> _checkIntervalProperty;

    private List<ServerStatusChangeListener> _listeners = new CopyOnWriteArrayList<>();

    public DefaultServerSourceMonitor(LoadBalancerContext loadBalancerContext) {
        NullArgumentChecker.DEFAULT.check(loadBalancerContext, "loadBalancerContext");

        _loadBalancerContext = loadBalancerContext;
        _additionInfo = loadBalancerContext.additionalInfo();
        _pingUtil = new PingUtil(_loadBalancerContext, _logger);

        TypedDynamicCachedCorrectedProperties properties = loadBalancerContext.properties();
        String managerId = loadBalancerContext.managerId();
        String loadBalancerId = loadBalancerContext.loadBalancerId();

        String propertyKey = generateKey(managerId, ConfigurationKeys.FailureThresholdPercentage);
        _managerLevelFailureThresholdPercentProperty = properties.getIntProperty(
                propertyKey, defaultFailureThresholdPercent, failureThresholdPercentLowerBound, failureThresholdPercentUpperBound);
        propertyKey = generateKey(managerId, ConfigurationKeys.CheckInterval);
        _managerLevelCheckIntervalProperty = properties.getIntProperty(
                propertyKey, defaultCheckInterval, checkIntervalLowerBound, checkIntervalUpperBound);

        propertyKey = generateKey(managerId, loadBalancerId, ConfigurationKeys.FailureThresholdPercentage);
        _failureThresholdPercentProperty = properties.getIntProperty(
                propertyKey, null, failureThresholdPercentLowerBound, failureThresholdPercentUpperBound);
        propertyKey = generateKey(managerId, loadBalancerId, ConfigurationKeys.CheckInterval);
        _checkIntervalProperty = properties.getIntProperty(
                propertyKey, null, checkIntervalLowerBound, checkIntervalUpperBound);


        PropertyChangeListener listener = new PropertyChangeListener() {
            @Override
            public void onChange(PropertyChangeEvent event) {
                resetCheckHealthExecutor();
            }
        };
        _managerLevelCheckIntervalProperty.addChangeListener(listener);
        _checkIntervalProperty.addChangeListener(listener);

        _checkHealthLatencyMetrics = new ConcurrentHashMap<>();
        _checkHealthEventMetrics = new ConcurrentHashMap<>();
    }

    private int failureThresholdPercent() {
        return getTypedValue(_managerLevelFailureThresholdPercentProperty, _failureThresholdPercentProperty);
    }

    private int checkIntervalInMillisecond() {
        return getTypedValue(_managerLevelCheckIntervalProperty, _checkIntervalProperty);
    }

    private boolean checkHealth(LoadBalancerRoute route, Server server) {
        if (!server.isAlive()) {
            if (!_pingUtil.hasPing())
                return server.isAlive();

            boolean pingSuccess = _pingUtil.isAlive(server);
            if (pingSuccess) {
                server.setAlive(true);

                String message = String.format("Server(%s) is available due to ping success", server.toString());
                LogUtil.info(_logger, message, getAdditionInfo(AvailabilityKey, Boolean.toString(true)));

                EventMetric checkHealthLatencyMetric = getCheckHealthEventMetric(route, server.getServerId());
                checkHealthLatencyMetric.addEvent("PullIn");
            }
            return pingSuccess;
        } else {
            if (route.getAvailableServers().size() <= _loadBalancerContext.getMinAvailableServerCount())
                return true;
            
            ServerStats serverStats = _loadBalancerContext.getServerStats(server);
            long successCount = serverStats.getAvailableCount();
            long failureCount = serverStats.getUnavailableCount();
            long totalCount = successCount + failureCount;
            if (totalCount == 0)
                return server.isAlive();
    
            long failurePercent = (int) (failureCount * 100.0 / totalCount);
            boolean failureRateExceeded = failurePercent >= failureThresholdPercent();
            if (!failureRateExceeded)
                return true;

            if (!_pingUtil.hasPing()) {
                server.setAlive(false);
                String messageFormat = "Server(%s) is unavailable due to high failure rate(%d%%)";
                String message = String.format(messageFormat, server.toString(), failurePercent);
                LogUtil.warn(_logger, message, getAdditionInfo(AvailabilityKey, Boolean.toString(false)));

                EventMetric checkHealthLatencyMetric = getCheckHealthEventMetric(route, server.getServerId());
                checkHealthLatencyMetric.addEvent("PullOut");
                return false;
            }

            boolean pingSuccess = _pingUtil.isAlive(server);
            if (!pingSuccess) {
                server.setAlive(false);
                String messageFormat = "Server(%s) is unavailable due to high failure rate(%d%%) and ping failure";
                String message = String.format(messageFormat, server.toString(), failurePercent);
                LogUtil.warn(_logger, message, getAdditionInfo(AvailabilityKey, Boolean.toString(false)));

                EventMetric checkHealthLatencyMetric = getCheckHealthEventMetric(route, server.getServerId());
                checkHealthLatencyMetric.addEvent("PullOut");
            }
            return pingSuccess;
        }
    }

    private boolean checkHealth(LoadBalancerRoute route) {
        boolean statusChanged = false;
        long startTime = System.currentTimeMillis();
        Map<String, String> additionInfo = getAdditionInfo("RouteId", route.getRouteId());

        List<Server> servers = route.getServers();
        if (servers.isEmpty())
            return false;

        List<Server> availableServers = route.getAvailableServers();
        if(availableServers.isEmpty()) {
            String message = "There is no available server in the list. Need check health.";
            LogUtil.warn(_logger, message, additionInfo);
        }

        int checkedCount = 0;
        int availableCount = 0;
        for (Server server : servers) {
            try {
                boolean isAlive = server.isAlive();
                if (isAlive != checkHealth(route, server)) {
                    statusChanged = true;
                }
                if (server.isAlive())
                    ++availableCount;
                ++checkedCount;
            } catch (Throwable t) {
                LogUtil.warn(_logger, "Error occurred while check server", t, additionInfo);
            }
        }

        long delta = System.currentTimeMillis() - startTime;
        AuditMetric checkHealthLatencyMetric = getCheckHealthLatencyMetric(route);
        checkHealthLatencyMetric.addValue(delta);

        boolean hasUnavailableServers = checkedCount != servers.size() || availableCount != servers.size();
        if (hasUnavailableServers || _lastHasUnavailableServers) {
            String messageFormat = "%d servers checked (%d/%d) in route:%s";
            String message = String.format(messageFormat, checkedCount, availableCount, servers.size(), route.getRouteId());
            LogUtil.info(_logger, message, additionInfo);
        }
        _lastHasUnavailableServers = hasUnavailableServers;
        
        return statusChanged;
    }

    private void runScheduledTask() {
        boolean statusChanged = false;
        List<LoadBalancerRoute> routes = _loadBalancerContext.serverSourceFilter().getLoadBalancerRoutes();
        for (LoadBalancerRoute route : routes) {
            statusChanged |= checkHealth(route);
        }
        if (statusChanged) {
            raisingServerStatusChangeEvent();
        }
    }

    @Override
    public void monitorServers() {
        if (_checkHealthFuture == null) {
            synchronized (this) {
                if (_checkHealthFuture == null) {
                    resetCheckHealthExecutor();
                }
            }
        }
    }

    private void resetCheckHealthExecutor() {

        int interval = checkIntervalInMillisecond();
        if (_interval == interval)
            return;

        if (_checkHealthFuture != null) {
            String message = String.format("Interval changed from %s to %s", _interval, interval);
            LogUtil.info(_logger, message, _additionInfo);
            _checkHealthFuture.cancel(false);
        }

        try {
            _interval = interval;
            _checkHealthFuture = _loadBalancerContext.getCheckHealthExecutorService().scheduleWithFixedDelay(new Runnable() {
                @Override
                public void run() {
                    try {
                        runScheduledTask();
                    } catch (Throwable t) {
                        LogUtil.warn(_logger, "Error occurred while check health", t, _additionInfo);
                    }
                }
            }, _interval, _interval, TimeUnit.MILLISECONDS);

            String message = String.format("Monitor task started with interval:%s", _interval);
            LogUtil.info(_logger, message, _additionInfo);
        } catch (Throwable t) {
            String message = String.format("Error occurred while starting monitor task! Interval:%s", _interval);
            LogUtil.error(_logger, message, t, _additionInfo);
        }
    }

    @Override
    public boolean isMonitoring() {
        return _checkHealthFuture != null && !_checkHealthFuture.isDone();
    }

    @Override
    public void addServerStatusChangeListener(ServerStatusChangeListener listener) {
        if (listener == null)
            return;
        
        _listeners.add(listener);
    }

    private void raisingServerStatusChangeEvent() {
        if (_listeners.isEmpty())
            return;

        LogUtil.info(_logger, "Raising server status change event...", _additionInfo);
        ServerStatusChangeEvent event = new ServerStatusChangeEvent();
        for (ServerStatusChangeListener listener : _listeners) {
            try {
                listener.onChange(event);
            } catch (Throwable t) {
                String message = "Error occurred while raising server status change event.";
                LogUtil.warn(_logger, message, t, _additionInfo);
            }
        }
        LogUtil.info(_logger, "Server status change event is completed!", _additionInfo);
    }

    private AuditMetric getCheckHealthLatencyMetric(LoadBalancerRoute route) {
        String managerId = _loadBalancerContext.managerId();
        String loadBalancerId = _loadBalancerContext.loadBalancerKey();

        final String metricId = generateKey(managerId, loadBalancerId , CheckHealthLatencyMetricSuffix);
        String metricName = generateKey(managerId, CheckHealthLatencyMetricSuffix);
        String distributionMetricName = generateKey(managerId, CheckHealthLatencyMetricSuffix, "distribution");
        final HashMap<String, String> metadata = new HashMap<>();
        metadata.put("metric_name_audit", metricName);
        metadata.put("metric_name_distribution", distributionMetricName);
        metadata.put("loadbalancerid", loadBalancerId);
        metadata.put("routeid", route.getRouteId());
        return ConcurrentHashMapValues.getOrAddWithLock(_checkHealthLatencyMetrics, route.getRouteId(), new Func<AuditMetric>() {
            @Override
            public AuditMetric execute() {
                return _loadBalancerContext.auditMetricManager().getMetric(metricId, new MetricConfig(metadata));
            }
        });
    }

    private EventMetric getCheckHealthEventMetric(LoadBalancerRoute route, String serverId) {
        String managerId = _loadBalancerContext.managerId();
        String loadBalancerId = _loadBalancerContext.loadBalancerKey();

        final String metricId = generateKey(managerId, loadBalancerId , CheckHealthEventMetricSuffix);
        String distributionMetricName = generateKey(managerId, CheckHealthEventMetricSuffix, "distribution");
        final HashMap<String, String> metadata = new HashMap<>();
        metadata.put("metric_name_distribution", distributionMetricName);
        metadata.put("loadbalancerid", loadBalancerId);
        metadata.put("routeid", route.getRouteId());
        metadata.put("serverid", serverId);
        return ConcurrentHashMapValues.getOrAddWithLock(_checkHealthEventMetrics, route.getRouteId(), new Func<EventMetric>() {
            @Override
            public EventMetric execute() {
                return _loadBalancerContext.eventMetricManager().getMetric(metricId, new MetricConfig(metadata));
            }
        });
    }

    private Map<String, String> getAdditionInfo(String key, String value) {
        Map<String, String> additionalInfo = _loadBalancerContext.additionalInfo();
        additionalInfo = Maps.newHashMap(additionalInfo);
        additionalInfo.put(key, value);
        return additionalInfo;
    }
}
