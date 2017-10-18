package com.ctrip.soa.caravan.common.concurrent.collect.circularbuffer.timebucket;

import java.util.ArrayList;
import java.util.List;

import com.ctrip.soa.caravan.common.delegate.Action1;
import com.ctrip.soa.caravan.common.value.checker.NullArgumentChecker;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class PercentileBuffer<T> extends TimeCircularBuffer<PercentileBucket<T>> {

    public PercentileBuffer(PercentileBufferConfig bufferConfig) {
        super(bufferConfig);
    }

    @Override
    protected PercentileBufferConfig bufferConfig() {
        return (PercentileBufferConfig) super.bufferConfig();
    }

    @Override
    protected PercentileBucket<T> createBucket(long startTime) {
        return new PercentileBucket<T>(startTime, bufferConfig().bucketCapacity());
    }

    public void add(T data) {
        currentBucket().add(data);
    }

    public void visitData(final Action1<T> action) {
        NullArgumentChecker.DEFAULT.check(action, "action");

        forEach(new Action1<PercentileBucket<T>>() {
            @Override
            public void execute(PercentileBucket<T> bucket) {
                for (int i = 0; i < bucket.count(); i++) {
                    T item = bucket.get(i);
                    if (item == null)
                        continue;
                    action.execute(item);
                }
            }
        });
    }

    public List<T> getSnapShot() {
        final List<T> snapShot = new ArrayList<T>();
        visitData(new Action1<T>() {
            @Override
            public void execute(T value) {
                snapShot.add(value);
            }
        });

        return snapShot;
    }

}
