package com.ctrip.soa.caravan.util.net.apache;

import java.util.concurrent.TimeUnit;

import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class AutoCleanedPoolingHttpClientConnectionManager extends PoolingHttpClientConnectionManager {

    private IdleConnectionMonitorThread _idleConnectionMonitorThread;

    public AutoCleanedPoolingHttpClientConnectionManager() {
        this(0, 0, 0, 0);
    }

    public AutoCleanedPoolingHttpClientConnectionManager(int connectionTtl, int inactivityTimeBeforeValidate, int connectionIdleTime, int cleanCheckInterval) {
        super(connectionTtl > 0 ? connectionTtl : DefaultConnectionConfig.DEFAULT_CONNECTION_TTL, TimeUnit.MILLISECONDS);

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
