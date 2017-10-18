package com.ctrip.soa.caravan.common.concurrent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ctrip.soa.caravan.common.value.corrector.RangeCorrector;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public final class Threads {

    private static final Logger _logger = LoggerFactory.getLogger(Threads.class);

    private static final RangeCorrector<Integer> _msRangeCorrector = new RangeCorrector<Integer>(0, Integer.MAX_VALUE);

    private static final RangeCorrector<Integer> _nanosRangeCorrector = new RangeCorrector<Integer>(0, 999999);

    private Threads() {

    }

    public static void sleep(int ms) {
        sleep(ms, 0);
    }

    public static void sleep(int ms, int nanos) {
        try {
            ms = _msRangeCorrector.correct(ms);
            nanos = _nanosRangeCorrector.correct(nanos);
            Thread.sleep(ms, nanos);
        } catch (Throwable ex) {
            _logger.error("Sleep failed.", ex);
        }
    }

}
