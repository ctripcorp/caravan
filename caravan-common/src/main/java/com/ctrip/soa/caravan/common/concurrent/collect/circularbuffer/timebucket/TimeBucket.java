package com.ctrip.soa.caravan.common.concurrent.collect.circularbuffer.timebucket;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public abstract class TimeBucket {

    private volatile long _startTime;

    protected TimeBucket(long startTime) {
        _startTime = startTime;
    }

    public long startTime() {
        return _startTime;
    }

    public void reset(long startTime) {
        _startTime = startTime;
    }

}
