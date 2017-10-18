package com.ctrip.soa.caravan.common.concurrent;

import java.io.Closeable;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class ValueScope<T> {

    private T _default;

    private ThreadLocal<T> _current = new ThreadLocal<>();

    private CloseableScope _scope = new CloseableScope(this);

    public ValueScope() {
        this(null);
    }

    public ValueScope(T defaultValue) {
        _default = defaultValue;
    }

    public T current() {
        T current = _current.get();
        return current == null ? _default : current;
    }

    public CloseableScope use(T value) {
        _current.set(value);
        return _scope;
    }

    public static class CloseableScope implements Closeable {

        private ValueScope<?> _valueScope;

        private CloseableScope(ValueScope<?> valueScope) {
            _valueScope = valueScope;
        }

        @Override
        public void close() {
            _valueScope._current.set(null);
        }

    }

}
