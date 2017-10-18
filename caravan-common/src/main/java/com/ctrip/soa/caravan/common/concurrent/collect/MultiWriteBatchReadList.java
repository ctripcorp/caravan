package com.ctrip.soa.caravan.common.concurrent.collect;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.ctrip.soa.caravan.common.value.CollectionValues;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class MultiWriteBatchReadList<V> {

    private volatile AtomicInteger _index = new AtomicInteger();

    private volatile ConcurrentHashMap<Integer, V> _data;

    public MultiWriteBatchReadList() {
        this(0);
    }

    public MultiWriteBatchReadList(int initCapacity) {
        if (initCapacity <= 0)
            _data = new ConcurrentHashMap<>();

        _data = new ConcurrentHashMap<>(initCapacity);
    }

    public void add(V value) {
        if (value == null)
            return;

        _data.put(_index.getAndIncrement(), value);
    }

    public void addAll(List<V> values) {
        if (CollectionValues.isNullOrEmpty(values))
            return;

        for (V value : values) {
            add(value);
        }
    }

    public int size() {
        return _index.get();
    }

    public List<V> getAll() {
        List<V> values = new ArrayList<>();

        for (int index = 0, lengthCandidate = _index.get(); index < lengthCandidate; index++) {
            V item = _data.get(index);
            if (item == null)
                continue;

            values.add(item);
        }

        return values;
    }

}
