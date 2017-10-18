package com.ctrip.soa.caravan.common.concurrent.collect.circularbuffer.timebucket;

import java.util.concurrent.atomic.AtomicLong;

import com.ctrip.soa.caravan.common.delegate.Action1;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class CounterBuffer<T> extends TimeCircularBuffer<CounterBucket<T>> {

    public CounterBuffer(TimeBufferConfig bufferConfig) {
        super(bufferConfig);
    }

    @Override
    protected CounterBucket<T> createBucket(long startTime) {
        return new CounterBucket<T>(startTime);
    }

    public long getCount(final T identity) {
        final AtomicLong count = new AtomicLong();
        forEach(new Action1<CounterBucket<T>>() {
            @Override
            public void execute(CounterBucket<T> bucket) {
                count.addAndGet(bucket.getCount(identity));
            }
        });

        return count.get();
    }

    public void incrementCount(T identity) {
        currentBucket().incrementCount(identity);
    }

    public void decrementCount(T identity) {
        currentBucket().decrementCount(identity);
    }

}
