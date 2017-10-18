package com.ctrip.soa.caravan.configuration.dynamic;

import java.util.ArrayList;
import java.util.List;

import com.ctrip.soa.caravan.configuration.ConfigurationSource;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class DefaultConfigurationSourceChangeEvent implements ConfigurationSourceChangeEvent {
    
    private ConfigurationSource _source;
    private long _changedTimeInMs;
    private List<PropertyChangeEvent> _events;

    public DefaultConfigurationSourceChangeEvent(ConfigurationSource source) {
        this(source, new ArrayList<PropertyChangeEvent>(), System.currentTimeMillis());
    }

    public DefaultConfigurationSourceChangeEvent(ConfigurationSource source, List<PropertyChangeEvent> events) {
        this(source, events, System.currentTimeMillis());
    }

    public DefaultConfigurationSourceChangeEvent(ConfigurationSource source, List<PropertyChangeEvent> events, long changedTimeInMs) {
        _source = source;
        _events = events;
        _changedTimeInMs = changedTimeInMs;
    }

    @Override
    public long changedTimeInMs() {
        return _changedTimeInMs;
    }

    @Override
    public ConfigurationSource source() {
        return _source;
    }

    @Override
    public List<PropertyChangeEvent> propertyChangeEvents() {
        return _events;
    }

}
