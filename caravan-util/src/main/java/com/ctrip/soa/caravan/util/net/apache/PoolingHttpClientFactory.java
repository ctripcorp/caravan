package com.ctrip.soa.caravan.util.net.apache;

import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.RequestContent;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class PoolingHttpClientFactory {

    private PoolingHttpClientFactory() {
        super();
    }

    public static CloseableHttpClient create() {
        return create(RequestConfigs.DEFAULT, IOExceptionRetryHelper.DEFAULT.retryHandler(), new AutoCleanedPoolingHttpClientConnectionManager());
    }

    public static CloseableHttpClient create(int connectTimeout, int connectionRequestTimeout, int socketTimeout, int maxConnectionsPerRoute,
            int maxTotalConnections) {
        RequestConfig config = RequestConfig.custom().setConnectTimeout(connectTimeout).setConnectionRequestTimeout(connectionRequestTimeout)
                .setSocketTimeout(socketTimeout).build();
        return create(config, IOExceptionRetryHelper.DEFAULT.retryHandler(), maxConnectionsPerRoute, maxTotalConnections, 0, 0, 0, 0);
    }

    public static CloseableHttpClient create(RequestConfig config, HttpRequestRetryHandler retryHandler, int maxConnectionsPerRoute, int maxTotalConnections,
            int connectionTtl, int inactivityTimeBeforeValidate, int connectionIdleTime, int cleanCheckInteval) {
        AutoCleanedPoolingHttpClientConnectionManager manager = new AutoCleanedPoolingHttpClientConnectionManager(connectionTtl, inactivityTimeBeforeValidate,
                connectionIdleTime, cleanCheckInteval);
        manager.setDefaultMaxPerRoute(maxConnectionsPerRoute);
        manager.setMaxTotal(maxTotalConnections);
        return create(config, retryHandler, manager);
    }

    public static CloseableHttpClient create(RequestConfig config, HttpRequestRetryHandler retryHandler, HttpClientConnectionManager connectionManager) {
        HttpClientBuilder builder = HttpClientBuilder.create();
        builder.useSystemProperties().setRedirectStrategy(AlwaysRedirectStrategy.DEFAULT).addInterceptorLast(new RequestContent(true));
        if (config != null)
            builder.setDefaultRequestConfig(config);
        if (retryHandler != null)
            builder.setRetryHandler(retryHandler);
        if (connectionManager != null)
            builder.setConnectionManager(connectionManager);
        return builder.build();
    }

}