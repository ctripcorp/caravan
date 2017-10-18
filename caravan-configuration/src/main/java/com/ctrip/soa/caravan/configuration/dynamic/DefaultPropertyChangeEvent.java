package com.ctrip.soa.caravan.configuration.dynamic;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class DefaultPropertyChangeEvent implements PropertyChangeEvent {
    
    private long _changedTimeInMs;
    private String _key;
    private String _oldValue;
    private String _newValue;
    
    public DefaultPropertyChangeEvent(String key, String oldValue, String newValue) {
        this(key, oldValue, newValue, System.currentTimeMillis());
    }

    public DefaultPropertyChangeEvent(String key, String oldValue, String newValue, long changedTimeInMs) {
        _key = key;
        _oldValue = oldValue;
        _newValue = newValue;
        _changedTimeInMs = changedTimeInMs;
    }

    @Override
    public long changedTimeInMs() {
        return _changedTimeInMs;
    }

    @Override
    public String key() {
        return _key;
    }

    @Override
    public String oldValue() {
        return _oldValue;
    }

    @Override
    public String newValue() {
        return _newValue;
    }
    
}
