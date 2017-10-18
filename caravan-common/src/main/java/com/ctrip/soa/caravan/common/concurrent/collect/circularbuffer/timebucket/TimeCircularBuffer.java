package com.ctrip.soa.caravan.common.concurrent.collect.circularbuffer.timebucket;

import java.util.concurrent.locks.ReentrantLock;

import com.ctrip.soa.caravan.common.collect.GenericArray;
import com.ctrip.soa.caravan.common.delegate.Action1;
import com.ctrip.soa.caravan.common.value.checker.NullArgumentChecker;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public abstract class TimeCircularBuffer<T extends TimeBucket> {

    private GenericArray<T> _buckets;
    private TimeBufferConfig _bufferConfig;

    private ReentrantLock _addBucketLock = new ReentrantLock();
    private volatile int _bufferEnd;
    private volatile T _spareBucket;

    protected TimeCircularBuffer(TimeBufferConfig bufferConfig) {
        NullArgumentChecker.DEFAULT.check(bufferConfig, "bufferConfig");

        _bufferConfig = bufferConfig;
        _buckets = new GenericArray<>(_bufferConfig.bucketCount() + 1);

        for (int i = 0; i < _buckets.length(); i++) {
            _buckets.set(i, createBucket(0));
        }

        _spareBucket = createBucket(0);
    }

    protected TimeBufferConfig bufferConfig() {
        return _bufferConfig;
    }

    protected abstract T createBucket(long startTime);

    protected final void forEach(Action1<T> action) {
        NullArgumentChecker.DEFAULT.check(action, "action");

        long currentBucketStartTime = getCurrentBucketStartTime();
        for (T bucket : _buckets) {
            if (isStaleBucket(bucket, currentBucketStartTime))
                continue;

            action.execute(bucket);
        }
    }

    protected final T currentBucket() {
        long currentBucketStartTime = getCurrentBucketStartTime();
        if (!shouldUpdateCurrentBucket(currentBucketStartTime))
            return _buckets.get(_bufferEnd);

        if (!_addBucketLock.tryLock())
            return _buckets.get(_bufferEnd);

        try {
            if (!shouldUpdateCurrentBucket(currentBucketStartTime))
                return _buckets.get(_bufferEnd);

            int newBufferEnd = (_bufferEnd + 1) % _buckets.length();
            T oldBucket = _buckets.get(newBufferEnd);
            _spareBucket.reset(currentBucketStartTime);
            _buckets.set(newBufferEnd, _spareBucket);
            _bufferEnd = newBufferEnd;
            oldBucket.reset(0);
            _spareBucket = oldBucket;
            return _buckets.get(_bufferEnd);
        } finally {
            _addBucketLock.unlock();
        }
    }

    private long getCurrentBucketStartTime() {
        long currentTime = System.currentTimeMillis();
        return currentTime - currentTime % _bufferConfig.bucketTimeWindow();
    }

    private boolean shouldUpdateCurrentBucket(long currentBucketStartTime) {
        return (_buckets.get(_bufferEnd).startTime() + _bufferConfig.bucketTimeWindow()) <= currentBucketStartTime;
    }

    private boolean isStaleBucket(T bucket, long currentBucketStartTime) {
        return bucket.startTime() + _bufferConfig.bufferTimeWindow() <= currentBucketStartTime;
    }

}
