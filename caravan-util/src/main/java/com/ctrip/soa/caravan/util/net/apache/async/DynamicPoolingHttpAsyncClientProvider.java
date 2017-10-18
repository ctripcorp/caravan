package com.ctrip.soa.caravan.util.net.apache.async;

import com.ctrip.soa.caravan.common.value.CloseableValues;
import com.ctrip.soa.caravan.configuration.facade.TypedDynamicCachedCorrectedProperties;
import com.ctrip.soa.caravan.configuration.util.RangePropertyConfig;
import com.ctrip.soa.caravan.util.net.apache.AbstractDynamicPoolingHttpClientProvider;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class DynamicPoolingHttpAsyncClientProvider extends AbstractDynamicPoolingHttpClientProvider<CloseableHttpAsyncClient> {

    public DynamicPoolingHttpAsyncClientProvider(String clientId, TypedDynamicCachedCorrectedProperties properties) {
        this(clientId, properties, DEFAULT_CONNECT_TIMEOUT_PROPERTY_CONFIG, DEFAULT_CONNECTION_REQUEST_TIMEOUT_PROPERTY_CONFIG,
                DEFAULT_SOCKET_TIMEOUT_PROPERTY_CONFIG, DEFAULT_MAX_CONNECTIONS_PER_ROUTE_PROPERTY_CONFIG, DEFAULT_MAX_TOTAL_CONNECTIONS_PROPERTY_CONFIG);
    }

    public DynamicPoolingHttpAsyncClientProvider(String clientId, TypedDynamicCachedCorrectedProperties properties,
            RangePropertyConfig<Integer> connectTimeoutPropertyConfig, RangePropertyConfig<Integer> connectionRequestTimeoutPropertyConfig,
            RangePropertyConfig<Integer> socketTimeoutPropertyConfig, RangePropertyConfig<Integer> maxConnectionsPerRoutePropertyConfig,
            RangePropertyConfig<Integer> maxTotalConnectionsPropertyConfig) {
        this(clientId, properties, connectTimeoutPropertyConfig, connectionRequestTimeoutPropertyConfig, socketTimeoutPropertyConfig,
                maxConnectionsPerRoutePropertyConfig, maxTotalConnectionsPropertyConfig, DEFAULT_CONNECTION_TTL_PROPERTY_CONFIG,
                DEFAULT_CONNECTION_IDLE_TIME_PROPERTY_CONFIG, DEFAULT_CLEAN_CHECK_INTERVAL_PROPERTY_CONFIG, DEFAULT_CLIENT_DESTROY_DELAY_PROPERTY_CONFIG);
    }

    public DynamicPoolingHttpAsyncClientProvider(String clientId, TypedDynamicCachedCorrectedProperties properties,
            RangePropertyConfig<Integer> connectTimeoutPropertyConfig, RangePropertyConfig<Integer> connectionRequestTimeoutPropertyConfig,
            RangePropertyConfig<Integer> socketTimeoutPropertyConfig, RangePropertyConfig<Integer> maxConnectionsPerRoutePropertyConfig,
            RangePropertyConfig<Integer> maxTotalConnectionsPropertyConfig, RangePropertyConfig<Integer> connectionTtlPropertyConfig,
            RangePropertyConfig<Integer> connectionIdleTimePropertyConfig, RangePropertyConfig<Integer> cleanCheckIntervalPropertyConfig,
            RangePropertyConfig<Integer> clientDestroyDelayPropertyConfig) {
        super(clientId, properties, connectTimeoutPropertyConfig, connectionRequestTimeoutPropertyConfig, socketTimeoutPropertyConfig,
                maxConnectionsPerRoutePropertyConfig, maxTotalConnectionsPropertyConfig, connectionTtlPropertyConfig, connectionIdleTimePropertyConfig,
                cleanCheckIntervalPropertyConfig, clientDestroyDelayPropertyConfig);
        updateClient();
    }

    @Override
    protected CloseableHttpAsyncClient createClient() {
        RequestConfig config = RequestConfig.custom().setConnectTimeout(_connectTimeoutProperty.typedValue().intValue())
                .setConnectionRequestTimeout(_connectionRequestTimeoutProperty.typedValue().intValue())
                .setSocketTimeout(_socketTimeoutProperty.typedValue().intValue()).build();
        CloseableHttpAsyncClient client = PoolingHttpAsyncClientFactory.create(config, _maxConnectionsPerRouteProperty.typedValue(),
                _maxTotalConectionsProperty.typedValue(), _connectionTtlProperty.typedValue(), _connectionIdleTimeProperty.typedValue(),
                _cleanCheckIntervalProperty.typedValue());
        try {
            client.start();
            return client;
        } catch (Throwable ex) {
            CloseableValues.close(client);
            throw ex;
        }
    }

}
