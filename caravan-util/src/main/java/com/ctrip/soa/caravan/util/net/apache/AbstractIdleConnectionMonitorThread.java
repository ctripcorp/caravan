package com.ctrip.soa.caravan.util.net.apache;

import java.io.Closeable;
import java.lang.ref.WeakReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ctrip.soa.caravan.common.concurrent.Threads;
import com.ctrip.soa.caravan.common.value.checker.NullArgumentChecker;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public abstract class AbstractIdleConnectionMonitorThread<T> extends Thread implements Closeable {

    private static final Logger _logger = LoggerFactory.getLogger(AbstractIdleConnectionMonitorThread.class);

    private WeakReference<T> _managerReference;
    private int _idleTime;
    private int _checkInterval;

    public AbstractIdleConnectionMonitorThread(T manager) {
        this(manager, 0, 0);
    }

    public AbstractIdleConnectionMonitorThread(T manager, int idleTime, int checkInterval) {
        NullArgumentChecker.DEFAULT.check(manager, "manager");

        _managerReference = new WeakReference<>(manager);
        _idleTime = idleTime > 0 ? idleTime : DefaultConnectionConfig.DEFAULT_CONNECTION_IDLE_TIME;
        _checkInterval = checkInterval > 0 ? checkInterval : DefaultConnectionConfig.DEFAULT_CLEAN_CHECK_INTERVAL;

        setDaemon(true);
    }

    @Override
    public void run() {
        for (T m = _managerReference.get(); m != null; m = _managerReference.get()) {
            try {
                closeIdleConnection(m);
            } catch (Throwable ex) {
                _logger.warn("Error closing idle connections", ex);
            }

            Threads.sleep(_checkInterval);
        }
    }

    @Override
    public void close() {
        _managerReference.clear();
    }

    protected int idleTime() {
        return _idleTime;
    }

    protected abstract void closeIdleConnection(T manager);

}
