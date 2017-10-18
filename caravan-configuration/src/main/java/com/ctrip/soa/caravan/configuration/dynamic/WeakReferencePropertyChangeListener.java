package com.ctrip.soa.caravan.configuration.dynamic;

import java.lang.ref.WeakReference;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public abstract class WeakReferencePropertyChangeListener<T> implements PropertyChangeListener {

    private WeakReference<T> _weakReference;

    public WeakReferencePropertyChangeListener(T data) {
        _weakReference = new WeakReference<>(data);
    }

    protected T data() {
        return _weakReference.get();
    }

    public boolean hasData() {
        return _weakReference.get() != null;
    }

}
