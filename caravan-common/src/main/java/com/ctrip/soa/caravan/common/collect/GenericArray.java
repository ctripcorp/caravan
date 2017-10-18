package com.ctrip.soa.caravan.common.collect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class GenericArray<T> implements Iterable<T> {

    private List<T> _data;
    private int _length;

    public GenericArray(int length) {
        if (length < 0)
            throw new IllegalArgumentException("Array length cannot be less than 0.");

        _length = length;
        _data = new ArrayList<>(_length);
        for (int index = 0; index < _length; index++) {
            _data.add(null);
        }
    }

    public void set(int index, T element) {
        checkIndex(index);
        _data.set(index, element);
    }

    public T get(int index) {
        checkIndex(index);
        return _data.get(index);
    }

    public int length() {
        return _length;
    }

    public T[] toArray(T[] a) {
        return _data.toArray(a);
    }

    public List<T> toList() {
        return Collections.unmodifiableList(_data);
    }

    public void clear() {
        for (int index = 0; index < _length; index++) {
            _data.set(index, null);
        }
    }

    @Override
    public Iterator<T> iterator() {
        return _data.iterator();
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= _length)
            throw new IndexOutOfBoundsException();
    }

}
