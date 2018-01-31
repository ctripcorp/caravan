package com.ctrip.soa.caravan.util.concurrent;

import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ctrip.soa.caravan.common.concurrent.Threads;
import com.ctrip.soa.caravan.common.defensive.Loops;
import com.ctrip.soa.caravan.common.delegate.Action;
import com.ctrip.soa.caravan.common.value.StringValues;
import com.ctrip.soa.caravan.common.value.checker.NullArgumentChecker;
import com.ctrip.soa.caravan.common.value.checker.StringArgumentChecker;
import com.ctrip.soa.caravan.configuration.typed.dynamic.TypedDynamicProperty;
import com.ctrip.soa.caravan.configuration.util.PropertyKeyGenerator;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class DynamicScheduledThread extends Thread {

    private static final Logger _logger = LoggerFactory.getLogger(DynamicScheduledThread.class);

    private String _threadId;
    private Runnable _runnable;
    private final TypedDynamicProperty<Integer> _initDelayProperty;
    private final TypedDynamicProperty<Integer> _runIntervalProperty;
    private final AtomicBoolean _isShutdown = new AtomicBoolean();

    public DynamicScheduledThread(String threadId, Runnable runnable, DynamicScheduledThreadConfig config) {
        StringArgumentChecker.DEFAULT.check(threadId, "threadId");
        NullArgumentChecker.DEFAULT.check(runnable, "runnable");
        NullArgumentChecker.DEFAULT.check(config, "config");

        _threadId = StringValues.trim(threadId);
        _runnable = runnable;

        setName(_threadId);

        String propertyKey = PropertyKeyGenerator.generateKey(_threadId, DynamicScheduledThreadConfig.INIT_DELAY_PROPERTY_KEY);
        _initDelayProperty = config.properties().getIntProperty(propertyKey, config.initDelayRange());

        propertyKey = PropertyKeyGenerator.generateKey(_threadId, DynamicScheduledThreadConfig.RUN_INTERVAL_PROPERTY_KEY);
        _runIntervalProperty = config.properties().getIntProperty(propertyKey, config.runIntervalRange());
    }

    @Override
    public final void run() {
        int initdelay = _initDelayProperty.typedValue().intValue();
        if (initdelay > 0)
            Threads.sleep(initdelay);

        while (!this.isInterrupted()) {
            if (_isShutdown.get())
                return;

            Loops.executeWithoutTightLoop(new Action() {
                @Override
                public void execute() {
                    try {
                        _runnable.run();
                    } catch (Throwable ex) {
                        _logger.error("failed to run scheduled runnable", ex);
                    }

                    if (_isShutdown.get())
                        return;

                    int runInterval = _runIntervalProperty.typedValue().intValue();
                    if (runInterval > 0)
                        Threads.sleep(runInterval);
                }
            });
        }
    }

    public void shutdown() {
        _isShutdown.set(true);
    }

}
