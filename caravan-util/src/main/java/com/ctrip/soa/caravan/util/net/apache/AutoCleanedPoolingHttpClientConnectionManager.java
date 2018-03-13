package com.ctrip.soa.caravan.util.net.apache;

import java.util.concurrent.TimeUnit;

import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpConnectionFactory;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class AutoCleanedPoolingHttpClientConnectionManager extends PoolingHttpClientConnectionManager {

    private static Registry<ConnectionSocketFactory> getDefaultRegistry() {
        return RegistryBuilder.<ConnectionSocketFactory> create().register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", SSLConnectionSocketFactory.getSocketFactory()).build();
    }

    private IdleConnectionMonitorThread _idleConnectionMonitorThread;

    public AutoCleanedPoolingHttpClientConnectionManager() {
        this(null);
    }

    public AutoCleanedPoolingHttpClientConnectionManager(HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory) {
        this(0, 0, 0, 0, connFactory);
    }

    public AutoCleanedPoolingHttpClientConnectionManager(int connectionTtl, int inactivityTimeBeforeValidate, int connectionIdleTime, int cleanCheckInterval) {
        this(connectionTtl, inactivityTimeBeforeValidate, connectionIdleTime, cleanCheckInterval, null);
    }

    public AutoCleanedPoolingHttpClientConnectionManager(int connectionTtl, int inactivityTimeBeforeValidate, int connectionIdleTime, int cleanCheckInterval,
            HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory) {
        super(getDefaultRegistry(), connFactory, null, null, connectionTtl > 0 ? connectionTtl : DefaultConnectionConfig.DEFAULT_CONNECTION_TTL,
                TimeUnit.MILLISECONDS);

        if (inactivityTimeBeforeValidate <= 0)
            inactivityTimeBeforeValidate = DefaultConnectionConfig.DEFAULT_INACTIVITY_TIME_BEFORE_VALIDATE;
        setValidateAfterInactivity(inactivityTimeBeforeValidate);

        _idleConnectionMonitorThread = new IdleConnectionMonitorThread(this, connectionIdleTime, cleanCheckInterval);
        _idleConnectionMonitorThread.start();
    }

    @Override
    public void shutdown() {
        _idleConnectionMonitorThread.close();
        super.shutdown();
    }

}
