package com.ctrip.soa.caravan.util.net.apache;

import java.util.concurrent.TimeUnit;

import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class IdleConnectionMonitorThread extends AbstractIdleConnectionMonitorThread<PoolingHttpClientConnectionManager> {

    private static final Logger _logger = LoggerFactory.getLogger(IdleConnectionMonitorThread.class);

    public IdleConnectionMonitorThread(PoolingHttpClientConnectionManager manager) {
        super(manager);
    }

    public IdleConnectionMonitorThread(PoolingHttpClientConnectionManager manager, int idleTime, int checkInterval) {
        super(manager, idleTime, checkInterval);
    }

    @Override
    protected void closeIdleConnection(PoolingHttpClientConnectionManager manager) {
        try {
            manager.closeExpiredConnections();
        } catch (final Throwable t) {
            _logger.warn("Error closing expired connections for sync pool", t);
        }

        try {
            manager.closeIdleConnections(idleTime(), TimeUnit.MILLISECONDS);
        } catch (final Throwable t) {
            _logger.warn("Error closing idle connections for sync pool", t);
        }
    }

}
