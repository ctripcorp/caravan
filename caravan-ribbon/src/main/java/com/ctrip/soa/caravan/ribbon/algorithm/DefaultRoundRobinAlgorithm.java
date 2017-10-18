package com.ctrip.soa.caravan.ribbon.algorithm;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by w.jian on 2017/2/25.
 */
public class DefaultRoundRobinAlgorithm<T> implements RoundRobinAlgorithm<T> {

    private static final int COUNTER_LIMIT = Integer.MAX_VALUE / 2;

    private AtomicInteger _counter;

    public DefaultRoundRobinAlgorithm() {
        _counter = new AtomicInteger(new Random().nextInt(COUNTER_LIMIT));
    }

    @Override
    public T choose(List<T> source) {
        int count = source.size();
        if (count == 0) {
            return null;
        }

        int counterValue = _counter.getAndIncrement();
        if (counterValue > COUNTER_LIMIT)
            _counter.set(0);

        int index = counterValue % count;
        return source.get(index);
    }
}
