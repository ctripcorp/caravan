package com.ctrip.soa.caravan.ribbon.loadbalancer;

import com.ctrip.soa.caravan.common.delegate.Func;
import com.ctrip.soa.caravan.common.metric.AuditMetricManager;
import com.ctrip.soa.caravan.common.metric.EventMetricManager;
import com.ctrip.soa.caravan.common.metric.StatusMetricManager;
import com.ctrip.soa.caravan.common.value.ConcurrentHashMapValues;
import com.ctrip.soa.caravan.common.value.StringValues;
import com.ctrip.soa.caravan.configuration.facade.TypedDynamicCachedCorrectedProperties;
import com.ctrip.soa.caravan.configuration.typed.dynamic.TypedDynamicProperty;
import com.ctrip.soa.caravan.configuration.util.RangePropertyConfig;
import com.ctrip.soa.caravan.ribbon.*;
import com.ctrip.soa.caravan.ribbon.rule.DefaultLoadBalancerRuleFactoryManager;
import com.ctrip.soa.caravan.ribbon.server.ServerStats;
import com.ctrip.soa.caravan.ribbon.serversource.filter.DefaultServerSourceFilter;
import com.ctrip.soa.caravan.ribbon.serversource.filter.ServerContextChangeEvent;
import com.ctrip.soa.caravan.ribbon.serversource.filter.ServerContextChangeListener;
import com.ctrip.soa.caravan.ribbon.serversource.filter.ServerSourceFilter;
import com.ctrip.soa.caravan.ribbon.serversource.manager.DefaultServerSourceManager;
import com.ctrip.soa.caravan.ribbon.serversource.manager.ServerSourceManager;
import com.ctrip.soa.caravan.ribbon.serversource.monitor.DefaultServerSourceMonitor;
import com.ctrip.soa.caravan.ribbon.serversource.monitor.ServerSourceMonitor;
import com.ctrip.soa.caravan.ribbon.serversource.monitor.ServerStatusChangeEvent;
import com.ctrip.soa.caravan.ribbon.serversource.monitor.ServerStatusChangeListener;
import com.ctrip.soa.caravan.ribbon.util.LogUtil;
import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by w.jian on 2016/7/19.
 */
public class DefaultLoadBalancerContext implements LoadBalancerContext {

    private static Logger _logger = LoggerFactory.getLogger(DefaultLoadBalancerContext.class);

    private static final int MIN_AVAILABLE_SERVER_COUNT_DEFAULT = 2;
    private static final int MIN_AVAILABLE_SERVER_COUNT_LOWER_BOUND = 1;
    private static final int MIN_AVAILABLE_SERVER_COUNT_UPPER_BOUND = 100;
    
    private TypedDynamicProperty<Integer> _minAvailableServerCount;
    private TypedDynamicProperty<Integer> _globalMinAvailableServerCount;

    private ScheduledExecutorService _checkHealthExecutorService;
    
    private LoadBalancerManager _lbManager;

    private String _loadBalancerId;

    private String _loadBalancerKey;

    private Ping _ping;

    private LoadBalancerRuleFactoryManager _loadBalancerRuleFactoryManager;

    private LoadBalancer _loadBalancer;

    private LoadBalancerConfig _loadBalancerConfig;

    private ServerSource _serverSource;

    private ServerSourceFilter _serverSourceFilter;

    private ServerSourceManager _serverSourceManager;

    private ServerSourceMonitor _serverSourceMonitor;

    private TypedDynamicCachedCorrectedProperties _properties;

    private AuditMetricManager _auditMetricManager;

    private EventMetricManager _eventMetricManager;

    private StatusMetricManager<Double> _statusMetricManager;

    private ConcurrentHashMap<String, ServerStats> _serverStatsMap = new ConcurrentHashMap<>();

    private Map<String, String> _additionalInfo;

    @Override
    public String managerId() {
        return _lbManager.getManagerId();
    }

    @Override
    public String loadBalancerId() {
        return _loadBalancerId;
    }

    @Override
    public String loadBalancerKey() {
        if (_loadBalancerKey == null) {
            _loadBalancerKey = String.format("%s.%s", managerId(), loadBalancerId());
        }
        return _loadBalancerKey;
    }

    @Override
    public Ping ping() {
        return _ping;
    }

    @Override
    public LoadBalancerRule getLoadBalancerRule(String ruleId) {
        return _loadBalancerRuleFactoryManager.getLoadBalancerRule(ruleId);
    }

    @Override
    public LoadBalancer loadBalancer() {
        return _loadBalancer;
    }

    @Override
    public LoadBalancerConfig loadBalancerConfig() {
        return _loadBalancerConfig;
    }

    @Override
    public ServerSource serverSource() {
        return _serverSource;
    }

    @Override
    public ServerSourceFilter serverSourceFilter() {
        return _serverSourceFilter;
    }

    @Override
    public ServerSourceManager serverSourceManager() {
        return _serverSourceManager;
    }

    @Override
    public ServerSourceMonitor serverSourceMonitor() {
        return _serverSourceMonitor;
    }
    
    @Override
    public TypedDynamicCachedCorrectedProperties properties() {
        return _properties;
    }

    @Override
    public AuditMetricManager auditMetricManager() {
        return _auditMetricManager;
    }

    @Override
    public EventMetricManager eventMetricManager() {
        return _eventMetricManager;
    }

    public StatusMetricManager<Double> statusMetricManager() {
        return _statusMetricManager;
    }

    @Override
    public Map<String, String> additionalInfo() {
        return _additionalInfo;
    }

    @Override
    public int getMinAvailableServerCount() {
        Integer minAvailableServerCount = _minAvailableServerCount.typedValue();
        if (minAvailableServerCount != null)
            return minAvailableServerCount;
        
        return _globalMinAvailableServerCount.typedValue();
    }
    
    @Override
    public ScheduledExecutorService getCheckHealthExecutorService() {
        return _checkHealthExecutorService;
    }
    
    public DefaultLoadBalancerContext(LoadBalancerManager lbManager, String lbId, LoadBalancerConfig lbConfig, ScheduledExecutorService checkHealthExecutorService) {
        _auditMetricManager = lbManager.getLoadBalancerManagerConfig().getValueMetricManager();
        _eventMetricManager = lbManager.getLoadBalancerManagerConfig().getEventMetricManager();
        _statusMetricManager = lbManager.getLoadBalancerManagerConfig().getStatusMetricManager();
        _properties = lbManager.getLoadBalancerManagerConfig().getProperties();
    
        _lbManager = lbManager;
        _checkHealthExecutorService = checkHealthExecutorService;
        _loadBalancerId = lbId;
        _loadBalancerConfig = lbConfig;
        _ping = lbConfig.getPing();
        _serverSource = lbConfig.getServerSource();
        
        final String loadBalancerKey = String.format("%s.%s", managerId(), loadBalancerId());
        _additionalInfo = ImmutableMap.of("LoadBalancerKey", loadBalancerKey);

        initialize(lbConfig);
    }

    private void initialize(LoadBalancerConfig lbConfig) {

        initializeMinAvailableServerCount();
    
        _loadBalancerRuleFactoryManager = new DefaultLoadBalancerRuleFactoryManager(this);
        if (lbConfig.getRuleFactory() != null) {
            _loadBalancerRuleFactoryManager.registerLoadBalancerRuleFactory(lbConfig.getRuleFactory());
        }
        _serverSourceFilter = new DefaultServerSourceFilter(this);
        _serverSourceManager = new DefaultServerSourceManager(this);
        _serverSourceMonitor = new DefaultServerSourceMonitor(this);
        _loadBalancer = new DefaultLoadBalancer(this);

        _serverSourceFilter.addServerContextChangeListener(new ServerContextChangeListener() {
            @Override
            public void onChange(ServerContextChangeEvent event) {
                _serverSourceManager.backup(_serverSourceFilter.getLoadBalancerRoutes());
            }
        });
        _serverSourceMonitor.addServerStatusChangeListener(new ServerStatusChangeListener() {
            @Override
            public void onChange(ServerStatusChangeEvent event) {
                _serverSourceFilter.refresh();
                _serverSourceManager.backup(_serverSourceFilter.getLoadBalancerRoutes());
            }
        });
        _serverSourceManager.addRestoreListener(_loadBalancerConfig.getServerSourceRestoreListener());
        _serverSourceFilter.addSeekRouteListener(_loadBalancerConfig.getSeekRouteListener());

        ServerSource serverSource = serverSource();
        if (serverSource instanceof DynamicServerSource) {
            ((DynamicServerSource) serverSource).registerServerSourceChangeListener(new ServerSourceChangeListener() {
                @Override
                public void onChange(ServerSourceChangeEvent event) {
                    _serverSourceFilter.setLoadBalancerRoutes(_serverSource.getLoadBalancerRoutes());
                }
            });
        }

        List<LoadBalancerRoute> routes = serverSource.getLoadBalancerRoutes();
        List<Server> servers = new ArrayList<>();
        if (routes != null) {
            for (LoadBalancerRoute route : routes) {
                servers.addAll(route.getServers());
            }
        }
        if (servers.isEmpty()) {
            routes = _serverSourceManager.restore();
            for (LoadBalancerRoute route : routes) {
                servers.addAll(route.getServers());
            }
            String localCache = StringValues.join(servers, '\n');
            String message = String.format("Server list is null! Load server list from local cache. %n%s", localCache);
            LogUtil.warn(_logger, message, additionalInfo());
        }
        _serverSourceFilter.setLoadBalancerRoutes(routes);

        if (!servers.isEmpty()) {
            _serverSourceManager.backup(_serverSourceFilter.getLoadBalancerRoutes());
        }
        _serverSourceMonitor.monitorServers();
    }

    private void initializeMinAvailableServerCount() {
        String propertyKey = String.format("%s.%s", loadBalancerKey(), ConfigurationKeys.MinAvailableServerCount);
        String globalPropertyKey = String.format("%s.%s", managerId(), ConfigurationKeys.MinAvailableServerCount);
        
        RangePropertyConfig<Integer> propertyConfig =
                new RangePropertyConfig<>(null, MIN_AVAILABLE_SERVER_COUNT_LOWER_BOUND, MIN_AVAILABLE_SERVER_COUNT_UPPER_BOUND);
        _minAvailableServerCount = properties().getIntProperty(propertyKey, propertyConfig);
        
        RangePropertyConfig<Integer> globalPropertyConfig =
                new RangePropertyConfig<>(MIN_AVAILABLE_SERVER_COUNT_DEFAULT, MIN_AVAILABLE_SERVER_COUNT_LOWER_BOUND, MIN_AVAILABLE_SERVER_COUNT_UPPER_BOUND);
        _globalMinAvailableServerCount = properties().getIntProperty(globalPropertyKey, globalPropertyConfig);
    }

    @Override
    public ServerStats getServerStats(Server server) {
        return ConcurrentHashMapValues.getOrAddWithLock(_serverStatsMap, server.getServerId(), new Func<ServerStats>() {
            @Override
            public ServerStats execute() {
                return new ServerStats(DefaultLoadBalancerContext.this);
            }
        });
    }
}
