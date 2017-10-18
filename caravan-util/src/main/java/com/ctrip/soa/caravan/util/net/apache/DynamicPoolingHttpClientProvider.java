package com.ctrip.soa.caravan.util.net.apache;

import com.ctrip.soa.caravan.common.value.checker.NullArgumentChecker;
import com.ctrip.soa.caravan.configuration.facade.TypedDynamicCachedCorrectedProperties;
import com.ctrip.soa.caravan.configuration.typed.dynamic.TypedDynamicProperty;
import com.ctrip.soa.caravan.configuration.util.PropertyKeyGenerator;
import com.ctrip.soa.caravan.configuration.util.RangePropertyConfig;

import org.apache.http.NoHttpResponseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class DynamicPoolingHttpClientProvider extends AbstractDynamicPoolingHttpClientProvider<CloseableHttpClient> {

    public static final String INACTIVITY_TIME_BEFORE_VALIDATE_PROPERTY_KEY = "client.inactivity-time-before-validate";

    public static final RangePropertyConfig<Integer> DEFAULT_INACTIVITY_TIME_BEFORE_VALIDATE_PROPERTY_CONFIG = new RangePropertyConfig<Integer>(
            DefaultConnectionConfig.DEFAULT_INACTIVITY_TIME_BEFORE_VALIDATE, 1 * 1000, 10 * 60 * 1000);

    private TypedDynamicProperty<Integer> _inactivityTimeBeforeValidateProperty;

    private IOExceptionRetryHelper _retryHelper = new IOExceptionRetryHelper(NoHttpResponseException.class);

    public DynamicPoolingHttpClientProvider(String clientId, TypedDynamicCachedCorrectedProperties properties) {
        this(clientId, properties, DEFAULT_CONNECT_TIMEOUT_PROPERTY_CONFIG, DEFAULT_CONNECTION_REQUEST_TIMEOUT_PROPERTY_CONFIG,
                DEFAULT_SOCKET_TIMEOUT_PROPERTY_CONFIG, DEFAULT_MAX_CONNECTIONS_PER_ROUTE_PROPERTY_CONFIG, DEFAULT_MAX_TOTAL_CONNECTIONS_PROPERTY_CONFIG);
    }

    public DynamicPoolingHttpClientProvider(String clientId, TypedDynamicCachedCorrectedProperties properties,
            RangePropertyConfig<Integer> connectTimeoutPropertyConfig, RangePropertyConfig<Integer> connectionRequestTimeoutPropertyConfig,
            RangePropertyConfig<Integer> socketTimeoutPropertyConfig, RangePropertyConfig<Integer> maxConnectionsPerRoutePropertyConfig,
            RangePropertyConfig<Integer> maxTotalConnectionsPropertyConfig) {
        this(clientId, properties, connectTimeoutPropertyConfig, connectionRequestTimeoutPropertyConfig, socketTimeoutPropertyConfig,
                maxConnectionsPerRoutePropertyConfig, maxTotalConnectionsPropertyConfig, DEFAULT_CONNECTION_TTL_PROPERTY_CONFIG,
                DEFAULT_INACTIVITY_TIME_BEFORE_VALIDATE_PROPERTY_CONFIG, DEFAULT_CONNECTION_IDLE_TIME_PROPERTY_CONFIG,
                DEFAULT_CLEAN_CHECK_INTERVAL_PROPERTY_CONFIG, DEFAULT_CLIENT_DESTROY_DELAY_PROPERTY_CONFIG);
    }

    public DynamicPoolingHttpClientProvider(String clientId, TypedDynamicCachedCorrectedProperties properties,
            RangePropertyConfig<Integer> connectTimeoutPropertyConfig, RangePropertyConfig<Integer> connectionRequestTimeoutPropertyConfig,
            RangePropertyConfig<Integer> socketTimeoutPropertyConfig, RangePropertyConfig<Integer> maxConnectionsPerRoutePropertyConfig,
            RangePropertyConfig<Integer> maxTotalConnectionsPropertyConfig, RangePropertyConfig<Integer> connectionTtlPropertyConfig,
            RangePropertyConfig<Integer> inactivityTimeBeforeValidatePropertyConfig, RangePropertyConfig<Integer> connectionIdleTimePropertyConfig,
            RangePropertyConfig<Integer> cleanCheckIntervalPropertyConfig, RangePropertyConfig<Integer> clientDestroyDelayPropertyConfig) {
        super(clientId, properties, connectTimeoutPropertyConfig, connectionRequestTimeoutPropertyConfig, socketTimeoutPropertyConfig,
                maxConnectionsPerRoutePropertyConfig, maxTotalConnectionsPropertyConfig, connectionTtlPropertyConfig, connectionIdleTimePropertyConfig,
                cleanCheckIntervalPropertyConfig, clientDestroyDelayPropertyConfig);
        NullArgumentChecker.DEFAULT.check(inactivityTimeBeforeValidatePropertyConfig, "inactivityTimeBeforeValidatePropertyConfig");

        String propertyKey = PropertyKeyGenerator.generateKey(_clientId, INACTIVITY_TIME_BEFORE_VALIDATE_PROPERTY_KEY);
        _inactivityTimeBeforeValidateProperty = properties.getIntProperty(propertyKey, inactivityTimeBeforeValidatePropertyConfig);
        _inactivityTimeBeforeValidateProperty.addChangeListener(_propertyChangeListener);

        updateClient();
    }

    public IOExceptionRetryHelper ioExceptionRetryHelper() {
        return _retryHelper;
    }

    @Override
    protected CloseableHttpClient createClient() {
        RequestConfig config = RequestConfig.custom().setConnectTimeout(_connectTimeoutProperty.typedValue().intValue())
                .setConnectionRequestTimeout(_connectionRequestTimeoutProperty.typedValue().intValue())
                .setSocketTimeout(_socketTimeoutProperty.typedValue().intValue()).build();
        return PoolingHttpClientFactory.create(config, _retryHelper.retryHandler(), _maxConnectionsPerRouteProperty.typedValue(),
                _maxTotalConectionsProperty.typedValue(), _connectionTtlProperty.typedValue(), _inactivityTimeBeforeValidateProperty.typedValue(),
                _connectionIdleTimeProperty.typedValue(), _cleanCheckIntervalProperty.typedValue());
    }

}
