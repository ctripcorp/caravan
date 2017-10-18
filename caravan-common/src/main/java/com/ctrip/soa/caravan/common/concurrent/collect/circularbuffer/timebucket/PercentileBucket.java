package com.ctrip.soa.caravan.common.concurrent.collect.circularbuffer.timebucket;

import java.util.concurrent.atomic.AtomicInteger;

import com.ctrip.soa.caravan.common.collect.GenericArray;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class PercentileBucket<T> extends TimeBucket {

    private final GenericArray<T> _data;
    private final AtomicInteger _count;

    public PercentileBucket(long startTime, int capacity) {
        super(startTime);

        _data = new GenericArray<>(capacity);
        _count = new AtomicInteger();
    }

    public int count() {
        return Math.min(_count.get(), _data.length());
    }

    public void add(T value) {
        if (_data.length() == 0)
            return;

        if (value == null)
            return;

        int index = _count.getAndIncrement() % _data.length();
        _data.set(index, value);
    }

    public T get(int index) {
        return _data.get(index);
    }

    @Override
    public void reset(long startTime) {
        super.reset(startTime);
        _data.clear();
        _count.set(0);
    }

}
