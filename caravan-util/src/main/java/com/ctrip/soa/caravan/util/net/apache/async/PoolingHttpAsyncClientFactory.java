package com.ctrip.soa.caravan.util.net.apache.async;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.nio.conn.NHttpClientConnectionManager;
import org.apache.http.protocol.RequestContent;

import com.ctrip.soa.caravan.util.net.apache.AlwaysRedirectStrategy;
import com.ctrip.soa.caravan.util.net.apache.RequestConfigs;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class PoolingHttpAsyncClientFactory {

    private PoolingHttpAsyncClientFactory() {
        super();
    }

    public static CloseableHttpAsyncClient create() {
        return create(RequestConfigs.DEFAULT, new AutoCleanedPoolingNHttpClientConnectionManager());
    }

    public static CloseableHttpAsyncClient create(int connectTimeout, int connectionRequestTimeout, int socketTimeout, int maxConnectionsPerRoute,
            int maxTotalConnections) {
        RequestConfig config = RequestConfig.custom().setConnectTimeout(connectTimeout).setConnectionRequestTimeout(connectionRequestTimeout)
                .setSocketTimeout(socketTimeout).build();
        return create(config, maxConnectionsPerRoute, maxTotalConnections, 0, 0, 0);
    }

    public static CloseableHttpAsyncClient create(RequestConfig config, int maxConnectionsPerRoute, int maxTotalConnections, int connectionTtl,
            int connectionIdleTime, int cleanCheckInteval) {
        AutoCleanedPoolingNHttpClientConnectionManager manager = new AutoCleanedPoolingNHttpClientConnectionManager(connectionTtl, connectionIdleTime,
                cleanCheckInteval);
        manager.setDefaultMaxPerRoute(maxConnectionsPerRoute);
        manager.setMaxTotal(maxTotalConnections);
        return create(config, manager);
    }

    public static CloseableHttpAsyncClient create(RequestConfig config, NHttpClientConnectionManager connectionManager) {
        HttpAsyncClientBuilder builder = HttpAsyncClientBuilder.create();
        builder.useSystemProperties().setRedirectStrategy(AlwaysRedirectStrategy.DEFAULT).addInterceptorLast(new RequestContent(true));
        if (config != null)
            builder.setDefaultRequestConfig(config);
        if (connectionManager != null)
            builder.setConnectionManager(connectionManager);
        return builder.build();
    }

}