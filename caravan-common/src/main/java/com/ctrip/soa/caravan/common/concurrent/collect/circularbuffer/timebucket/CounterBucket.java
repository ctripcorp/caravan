package com.ctrip.soa.caravan.common.concurrent.collect.circularbuffer.timebucket;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import com.ctrip.soa.caravan.common.delegate.Func;
import com.ctrip.soa.caravan.common.value.ConcurrentHashMapValues;
import com.ctrip.soa.caravan.common.value.checker.NullArgumentChecker;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class CounterBucket<T> extends TimeBucket {

    private final ConcurrentHashMap<T, AtomicLong> _counters;

    public CounterBucket(long startTime) {
        super(startTime);
        _counters = new ConcurrentHashMap<T, AtomicLong>();
    }

    public long getCount(T identity) {
        NullArgumentChecker.DEFAULT.check(identity, "identity");
        AtomicLong counter = _counters.get(identity);
        return counter == null ? 0 : counter.get();
    }

    public void incrementCount(T identity) {
        NullArgumentChecker.DEFAULT.check(identity, "identity");
        AtomicLong counter = getCounter(identity);
        counter.incrementAndGet();
    }

    public void decrementCount(T identity) {
        NullArgumentChecker.DEFAULT.check(identity, "identity");
        AtomicLong counter = getCounter(identity);
        counter.decrementAndGet();
    }

    private AtomicLong getCounter(T identity) {
        return ConcurrentHashMapValues.getOrAdd(_counters, identity, new Func<AtomicLong>() {
            @Override
            public AtomicLong execute() {
                return new AtomicLong();
            }
        });
    }

    @Override
    public void reset(long startTime) {
        super.reset(startTime);
        _counters.clear();
    }

}
