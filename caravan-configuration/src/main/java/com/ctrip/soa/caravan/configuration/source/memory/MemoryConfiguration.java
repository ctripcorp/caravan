package com.ctrip.soa.caravan.configuration.source.memory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ctrip.soa.caravan.common.value.checker.NullArgumentChecker;
import com.ctrip.soa.caravan.configuration.Configuration;
import com.ctrip.soa.caravan.configuration.ConfigurationSource;
import com.ctrip.soa.caravan.configuration.dynamic.ConfigurationSourceChangeEvent;
import com.ctrip.soa.caravan.configuration.dynamic.ConfigurationSourceChangeListener;
import com.ctrip.soa.caravan.configuration.dynamic.DefaultConfigurationSourceChangeEvent;
import com.ctrip.soa.caravan.configuration.dynamic.DefaultPropertyChangeEvent;

/**
 * Created by w.jian on 2016/5/18.
 */
public class MemoryConfiguration implements Configuration {

    protected static final Logger _logger = LoggerFactory.getLogger(MemoryConfiguration.class);
    protected static final String _propertyChangedMessageFormatter = "Memory property %s changed from %s to %s";

    private ConfigurationSource _configurationSource;
    private final ConcurrentHashMap<String, String> _configurationMap;
    private final List<ConfigurationSourceChangeListener> _listeners;

    public MemoryConfiguration(ConfigurationSource source) {
        NullArgumentChecker.DEFAULT.check(source, "source");

        _configurationSource = source;
        _configurationMap = new ConcurrentHashMap<>();
        _listeners = new ArrayList<>();
    }

    @Override
    public String getPropertyValue(String key) {
        if (key == null) {
            return null;
        }
        return _configurationMap.get(key);
    }

    public void setPropertyValue(String key, String newValue) {
        NullArgumentChecker.DEFAULT.check(key, "key");

        String oldValue;
        if (newValue == null) {
            oldValue = _configurationMap.get(key);
            _configurationMap.remove(key);
        } else {
            oldValue = _configurationMap.put(key, newValue);
        }

        if (Objects.equals(oldValue, newValue)) {
            return;
        }

        _logger.info(String.format(_propertyChangedMessageFormatter, key, oldValue, newValue));
        DefaultConfigurationSourceChangeEvent event = new DefaultConfigurationSourceChangeEvent(_configurationSource);
        event.propertyChangeEvents().add(new DefaultPropertyChangeEvent(key, oldValue, newValue));
        raiseSourceChangeEvent(event);
    }

    public void setProperties(Map<String, String> keyValueMap) {

        DefaultConfigurationSourceChangeEvent event = new DefaultConfigurationSourceChangeEvent(_configurationSource);

        String key, newValue;
        for (Map.Entry<String, String> entry : keyValueMap.entrySet()) {
            key = entry.getKey();
            newValue = entry.getValue();

            String oldValue;
            if (newValue == null) {
                oldValue = _configurationMap.get(key);
                _configurationMap.remove(key);
            } else {
                oldValue = _configurationMap.put(key, newValue);
            }

            if (Objects.equals(oldValue, newValue)) {
                continue;
            }

            _logger.info(String.format(_propertyChangedMessageFormatter, key, oldValue, newValue));
            event.propertyChangeEvents().add(new DefaultPropertyChangeEvent(key, oldValue, newValue));
        }
        if (event.propertyChangeEvents().size() > 0) {
            raiseSourceChangeEvent(event);
        }
    }

    protected void raiseSourceChangeEvent(ConfigurationSourceChangeEvent event) {
        for (ConfigurationSourceChangeListener listener : _listeners) {
            listener.onChange(event);
        }
    }

    public synchronized void addSourceChangeListener(ConfigurationSourceChangeListener listener) {
        NullArgumentChecker.DEFAULT.check(listener, "listener");
        _listeners.add(listener);
    }
}