package com.ctrip.soa.caravan.util.net.apache.async;

import java.util.concurrent.TimeUnit;

import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ctrip.soa.caravan.util.net.apache.AbstractIdleConnectionMonitorThread;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class IdleNConnectionMonitorThread extends AbstractIdleConnectionMonitorThread<PoolingNHttpClientConnectionManager> {

    private static final Logger _logger = LoggerFactory.getLogger(IdleNConnectionMonitorThread.class);

    public IdleNConnectionMonitorThread(PoolingNHttpClientConnectionManager manager) {
        super(manager);
    }

    public IdleNConnectionMonitorThread(PoolingNHttpClientConnectionManager manager, int idleTime, int checkInterval) {
        super(manager, idleTime, checkInterval);
    }

    @Override
    protected void closeIdleConnection(PoolingNHttpClientConnectionManager manager) {
        try {
            manager.closeExpiredConnections();
        } catch (final Throwable t) {
            _logger.warn("Error closing expired connections for async pool", t);
        }

        try {
            manager.closeIdleConnections(idleTime(), TimeUnit.MILLISECONDS);
        } catch (final Throwable t) {
            _logger.warn("Error closing idle connections for async pool", t);
        }
    }

}
