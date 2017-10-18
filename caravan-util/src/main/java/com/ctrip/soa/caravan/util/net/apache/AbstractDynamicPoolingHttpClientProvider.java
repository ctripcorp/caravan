package com.ctrip.soa.caravan.util.net.apache;

import java.io.Closeable;
import java.util.concurrent.ExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ctrip.soa.caravan.common.concurrent.ThreadPools;
import com.ctrip.soa.caravan.common.concurrent.Threads;
import com.ctrip.soa.caravan.common.value.StringValues;
import com.ctrip.soa.caravan.common.value.checker.NullArgumentChecker;
import com.ctrip.soa.caravan.common.value.checker.StringArgumentChecker;
import com.ctrip.soa.caravan.configuration.dynamic.PropertyChangeEvent;
import com.ctrip.soa.caravan.configuration.dynamic.PropertyChangeListener;
import com.ctrip.soa.caravan.configuration.facade.TypedDynamicCachedCorrectedProperties;
import com.ctrip.soa.caravan.configuration.typed.dynamic.TypedDynamicProperty;
import com.ctrip.soa.caravan.configuration.util.PropertyKeyGenerator;
import com.ctrip.soa.caravan.configuration.util.RangePropertyConfig;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public abstract class AbstractDynamicPoolingHttpClientProvider<T extends Closeable> {

    private static final String CLIENT_DESTRORY_THREAD_NAME_FORMAT = "dynamic-pooling-httpclient-provider-client-destroy-%d";

    public static final String CONNECT_TIMEOUT_PROPERTY_KEY = "client.connect-timeout";
    public static final String CONNECTION_REQUEST_TIMEOUT_PROPERTY_KEY = "client.connection-request-timeout";
    public static final String SOCKET_TIMEOUT_PROPERTY_KEY = "client.socket-timeout";
    public static final String MAX_CONNECTIONS_PER_ROUTE_PROPERTY_KEY = "client.max-connections-per-route";
    public static final String MAX_TOTAL_CONNECTIONS_PROPERTY_KEY = "client.max-total-connections";
    public static final String CONNECTION_TTL_PROPERTY_KEY = "client.connection-ttl";
    public static final String CONNECTION_IDLE_TIME_PROPERTY_KEY = "client.connection-idle-time";
    public static final String CLEAN_CHECK_INTERVAL_PROPERTY_KEY = "client.clean-check-interval";
    public static final String CLIENT_DESTROY_DELAY_PROPERTY_KEY = "client.destroy-delay";

    public static final RangePropertyConfig<Integer> DEFAULT_CONNECT_TIMEOUT_PROPERTY_CONFIG = new RangePropertyConfig<Integer>(
            RequestConfigs.DEFAULT_CONNECT_TIMEOUT, 10, 1 * 60 * 1000);
    public static final RangePropertyConfig<Integer> DEFAULT_CONNECTION_REQUEST_TIMEOUT_PROPERTY_CONFIG = new RangePropertyConfig<Integer>(
            RequestConfigs.DEFAULT_CONNECTION_REQUEST_TIMEOUT, 5, 10 * 1000);
    public static final RangePropertyConfig<Integer> DEFAULT_SOCKET_TIMEOUT_PROPERTY_CONFIG = new RangePropertyConfig<Integer>(
            RequestConfigs.DEFAULT_SOCKET_TIMEOUT, 5, 1 * 60 * 60 * 1000);
    public static final RangePropertyConfig<Integer> DEFAULT_MAX_CONNECTIONS_PER_ROUTE_PROPERTY_CONFIG = new RangePropertyConfig<Integer>(
            DefaultConnectionConfig.DEFAULT_MAX_CONNECTIONS_PER_ROUTE, 1, 100 * 1000);
    public static final RangePropertyConfig<Integer> DEFAULT_MAX_TOTAL_CONNECTIONS_PROPERTY_CONFIG = new RangePropertyConfig<Integer>(
            DefaultConnectionConfig.DEFAULT_MAX_TOTAL_CONNECTIONS, 5, 1 * 1000 * 1000);
    public static final RangePropertyConfig<Integer> DEFAULT_CONNECTION_TTL_PROPERTY_CONFIG = new RangePropertyConfig<Integer>(
            DefaultConnectionConfig.DEFAULT_CONNECTION_TTL, 30 * 1000, 1 * 60 * 60 * 1000);
    public static final RangePropertyConfig<Integer> DEFAULT_CONNECTION_IDLE_TIME_PROPERTY_CONFIG = new RangePropertyConfig<Integer>(
            DefaultConnectionConfig.DEFAULT_CONNECTION_IDLE_TIME, 1000, 10 * 60 * 1000);
    public static final RangePropertyConfig<Integer> DEFAULT_CLEAN_CHECK_INTERVAL_PROPERTY_CONFIG = new RangePropertyConfig<Integer>(
            DefaultConnectionConfig.DEFAULT_CLEAN_CHECK_INTERVAL, 1000, 1 * 60 * 60 * 1000);
    public static final RangePropertyConfig<Integer> DEFAULT_CLIENT_DESTROY_DELAY_PROPERTY_CONFIG = new RangePropertyConfig<Integer>(60 * 1000, 0,
            10 * 60 * 1000);

    private static final Logger _logger = LoggerFactory.getLogger(AbstractDynamicPoolingHttpClientProvider.class);
    private static final ExecutorService _clientDestoryThreadPool = ThreadPools.newCachedDaemonThreadPool(CLIENT_DESTRORY_THREAD_NAME_FORMAT);

    protected final String _clientId;
    protected final TypedDynamicProperty<Integer> _connectTimeoutProperty;
    protected final TypedDynamicProperty<Integer> _connectionRequestTimeoutProperty;
    protected final TypedDynamicProperty<Integer> _socketTimeoutProperty;
    protected final TypedDynamicProperty<Integer> _maxConnectionsPerRouteProperty;
    protected final TypedDynamicProperty<Integer> _maxTotalConectionsProperty;
    protected final TypedDynamicProperty<Integer> _connectionTtlProperty;
    protected final TypedDynamicProperty<Integer> _connectionIdleTimeProperty;
    protected final TypedDynamicProperty<Integer> _cleanCheckIntervalProperty;
    protected final TypedDynamicProperty<Integer> _clientDestroyDelayProperty;
    protected final PropertyChangeListener _propertyChangeListener;

    private volatile T _client;

    public AbstractDynamicPoolingHttpClientProvider(String clientId, TypedDynamicCachedCorrectedProperties properties,
            RangePropertyConfig<Integer> connectTimeoutPropertyConfig, RangePropertyConfig<Integer> connectionRequestTimeoutPropertyConfig,
            RangePropertyConfig<Integer> socketTimeoutPropertyConfig, RangePropertyConfig<Integer> maxConnectionsPerRoutePropertyConfig,
            RangePropertyConfig<Integer> maxTotalConnectionsPropertyConfig, RangePropertyConfig<Integer> connectionTtlPropertyConfig,
            RangePropertyConfig<Integer> connectionIdleTimePropertyConfig, RangePropertyConfig<Integer> cleanCheckIntervalPropertyConfig,
            RangePropertyConfig<Integer> clientDestroyDelayPropertyConfig) {
        StringArgumentChecker.DEFAULT.check(clientId, "clientId");
        NullArgumentChecker.DEFAULT.check(properties, "properties");
        NullArgumentChecker.DEFAULT.check(connectTimeoutPropertyConfig, "connectTimeoutPropertyConfig");
        NullArgumentChecker.DEFAULT.check(connectionRequestTimeoutPropertyConfig, "connectionRequestTimeoutPropertyConfig");
        NullArgumentChecker.DEFAULT.check(socketTimeoutPropertyConfig, "socketTimeoutPropertyConfig");
        NullArgumentChecker.DEFAULT.check(maxConnectionsPerRoutePropertyConfig, "maxConnectionsPerRoutePropertyConfig");
        NullArgumentChecker.DEFAULT.check(maxTotalConnectionsPropertyConfig, "maxTotalConnectionsPropertyConfig");
        NullArgumentChecker.DEFAULT.check(connectionTtlPropertyConfig, "connectionTtlPropertyConfig");
        NullArgumentChecker.DEFAULT.check(connectionIdleTimePropertyConfig, "connectionIdleTimePropertyConfig");
        NullArgumentChecker.DEFAULT.check(cleanCheckIntervalPropertyConfig, "cleanCheckIntervalPropertyConfig");
        NullArgumentChecker.DEFAULT.check(clientDestroyDelayPropertyConfig, "clientDestroyDelayPropertyConfig");

        _clientId = StringValues.trim(clientId);

        String propertyKey = PropertyKeyGenerator.generateKey(_clientId, CONNECT_TIMEOUT_PROPERTY_KEY);
        _connectTimeoutProperty = properties.getIntProperty(propertyKey, connectTimeoutPropertyConfig);
        propertyKey = PropertyKeyGenerator.generateKey(_clientId, CONNECTION_REQUEST_TIMEOUT_PROPERTY_KEY);
        _connectionRequestTimeoutProperty = properties.getIntProperty(propertyKey, connectionRequestTimeoutPropertyConfig);
        propertyKey = PropertyKeyGenerator.generateKey(_clientId, SOCKET_TIMEOUT_PROPERTY_KEY);
        _socketTimeoutProperty = properties.getIntProperty(propertyKey, socketTimeoutPropertyConfig);
        propertyKey = PropertyKeyGenerator.generateKey(_clientId, MAX_CONNECTIONS_PER_ROUTE_PROPERTY_KEY);
        _maxConnectionsPerRouteProperty = properties.getIntProperty(propertyKey, maxConnectionsPerRoutePropertyConfig);
        propertyKey = PropertyKeyGenerator.generateKey(_clientId, MAX_TOTAL_CONNECTIONS_PROPERTY_KEY);
        _maxTotalConectionsProperty = properties.getIntProperty(propertyKey, maxTotalConnectionsPropertyConfig);
        propertyKey = PropertyKeyGenerator.generateKey(_clientId, CONNECTION_TTL_PROPERTY_KEY);
        _connectionTtlProperty = properties.getIntProperty(propertyKey, connectionTtlPropertyConfig);
        propertyKey = PropertyKeyGenerator.generateKey(_clientId, CONNECTION_IDLE_TIME_PROPERTY_KEY);
        _connectionIdleTimeProperty = properties.getIntProperty(propertyKey, connectionIdleTimePropertyConfig);
        propertyKey = PropertyKeyGenerator.generateKey(_clientId, CLEAN_CHECK_INTERVAL_PROPERTY_KEY);
        _cleanCheckIntervalProperty = properties.getIntProperty(propertyKey, cleanCheckIntervalPropertyConfig);
        propertyKey = PropertyKeyGenerator.generateKey(_clientId, CLIENT_DESTROY_DELAY_PROPERTY_KEY);
        _clientDestroyDelayProperty = properties.getIntProperty(propertyKey, clientDestroyDelayPropertyConfig);

        _propertyChangeListener = new PropertyChangeListener() {
            @Override
            public void onChange(PropertyChangeEvent event) {
                updateClient();
            }
        };

        _connectTimeoutProperty.addChangeListener(_propertyChangeListener);
        _connectionRequestTimeoutProperty.addChangeListener(_propertyChangeListener);
        _socketTimeoutProperty.addChangeListener(_propertyChangeListener);
        _maxConnectionsPerRouteProperty.addChangeListener(_propertyChangeListener);
        _maxTotalConectionsProperty.addChangeListener(_propertyChangeListener);
        _connectionTtlProperty.addChangeListener(_propertyChangeListener);
        _connectionIdleTimeProperty.addChangeListener(_propertyChangeListener);
        _cleanCheckIntervalProperty.addChangeListener(_propertyChangeListener);
    }

    public T get() {
        return _client;
    }

    protected void updateClient() {
        final T client = _client;
        _client = createClient();
        _logger.info("updated http client");

        if (client == null)
            return;

        _clientDestoryThreadPool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    int delay = _clientDestroyDelayProperty.typedValue().intValue();
                    if (delay > 0)
                        Threads.sleep(delay);

                    client.close();
                    _logger.info("Old httpclient is distroyed");
                } catch (Throwable ex) {
                    _logger.error("Close old httpclient failed.", ex);
                }
            }
        });
    }

    protected abstract T createClient();

}
