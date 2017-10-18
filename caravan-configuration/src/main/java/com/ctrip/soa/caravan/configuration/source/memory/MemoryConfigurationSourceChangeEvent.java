package com.ctrip.soa.caravan.configuration.source.memory;

import java.util.ArrayList;
import java.util.List;

import com.ctrip.soa.caravan.configuration.ConfigurationSource;
import com.ctrip.soa.caravan.configuration.dynamic.ConfigurationSourceChangeEvent;
import com.ctrip.soa.caravan.configuration.dynamic.PropertyChangeEvent;

/**
 * Created by w.jian on 2016/5/18.
 */
class MemoryConfigurationSourceChangeEvent implements ConfigurationSourceChangeEvent {

    private long _changedTimeInMs;
    private ConfigurationSource _configurationSource;
    private List<PropertyChangeEvent> _propertyChangeEvents;

    MemoryConfigurationSourceChangeEvent(){
        _propertyChangeEvents = new ArrayList<>();
    }

    @Override
    public long changedTimeInMs() {
        return _changedTimeInMs;
    }

    @Override
    public ConfigurationSource source() {
        return _configurationSource;
    }

    @Override
    public List<PropertyChangeEvent> propertyChangeEvents() {
        return _propertyChangeEvents;
    }

    void setChangedTimeInMs(long changedTimeInMs){
        _changedTimeInMs = changedTimeInMs;
    }

    void setConfigurationSource(ConfigurationSource configurationSource){
        _configurationSource = configurationSource;
    }

    void addPropertyChangeEvent(PropertyChangeEvent propertyChangeEvent){
        _propertyChangeEvents.add(propertyChangeEvent);
    }
}
