package com.ctrip.soa.caravan.common.defensive;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ctrip.soa.caravan.common.concurrent.Threads;
import com.ctrip.soa.caravan.common.delegate.Action;
import com.ctrip.soa.caravan.common.delegate.Func;
import com.ctrip.soa.caravan.common.value.checker.NullArgumentChecker;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public final class Loops {

    private static final Logger _logger = LoggerFactory.getLogger(Loops.class);

    private static final int DEFAULT_SLEEP_NANOS_IN_TIGHT_LOOP = 10 * 1000;

    private Loops() {

    }

    public static <V> V executeWithoutTightLoop(Func<V> func) {
        return executeWithoutTightLoop(func, 0, DEFAULT_SLEEP_NANOS_IN_TIGHT_LOOP);
    }

    public static <V> V executeWithoutTightLoop(Func<V> func, int ms, int nanos) {
        NullArgumentChecker.DEFAULT.check(func, "func");

        long startTime = System.currentTimeMillis();
        try {
            return func.execute();
        } finally {
            if (System.currentTimeMillis() - startTime <= 0)
                preventTightLoop(ms, nanos);
        }
    }

    public static void executeWithoutTightLoop(Action action) {
        executeWithoutTightLoop(action, 0, DEFAULT_SLEEP_NANOS_IN_TIGHT_LOOP);
    }

    public static void executeWithoutTightLoop(Action action, int ms, int nanos) {
        NullArgumentChecker.DEFAULT.check(action, "action");

        long startTime = System.currentTimeMillis();
        try {
            action.execute();
        } finally {
            if (System.currentTimeMillis() - startTime <= 0)
                preventTightLoop(ms, nanos);
        }
    }

    public static void preventTightLoop() {
        preventTightLoop(0, DEFAULT_SLEEP_NANOS_IN_TIGHT_LOOP);
    }

    public static void preventTightLoop(int ms, int nanos) {
        _logger.info("Sleep {} ms & {} nanos to prevent tight loop.", ms, nanos);
        Threads.sleep(ms, nanos);
    }

}
