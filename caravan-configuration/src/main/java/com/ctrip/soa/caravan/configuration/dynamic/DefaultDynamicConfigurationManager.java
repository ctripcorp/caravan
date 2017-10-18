package com.ctrip.soa.caravan.configuration.dynamic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ctrip.soa.caravan.common.concurrent.ThreadFactories;
import com.ctrip.soa.caravan.common.value.CollectionValues;
import com.ctrip.soa.caravan.configuration.ConfigurationManager;
import com.ctrip.soa.caravan.configuration.ConfigurationSource;
import com.ctrip.soa.caravan.configuration.util.ConfigurationSourceComparator;
import com.ctrip.soa.caravan.configuration.wrapper.ConfigurationManagerWrapper;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class DefaultDynamicConfigurationManager extends ConfigurationManagerWrapper implements DynamicConfigurationManager {

    private static final Logger _logger = LoggerFactory.getLogger(DefaultDynamicConfigurationManager.class);

    private ConcurrentHashMap<String, List<PropertyChangeListener>> _propertyChangeListeners;

    private List<PropertyChangeListener> _globalPropertyChangeListeners;

    private List<ConfigurationSource> _sortedSources;

    private ExecutorService _propertyChangeExecutorService = Executors.newFixedThreadPool(1, ThreadFactories.DEFAULT);
    private ScheduledExecutorService _propertyCleanExecutorService = Executors.newSingleThreadScheduledExecutor(ThreadFactories.DEFAULT);

    protected List<PropertyChangeListener> globalPropertyChangeListeners() {
        return _globalPropertyChangeListeners;
    }

    protected ConcurrentHashMap<String, List<PropertyChangeListener>> propertyChangeListeners() {
        return _propertyChangeListeners;
    }

    public DefaultDynamicConfigurationManager(ConfigurationManager manager) {
        super(manager);

        _globalPropertyChangeListeners = new CopyOnWriteArrayList<PropertyChangeListener>();
        _propertyChangeListeners = new ConcurrentHashMap<String, List<PropertyChangeListener>>();

        _sortedSources = new ArrayList<>(sources());
        Collections.sort(_sortedSources, ConfigurationSourceComparator.DEFAULT);

        for (ConfigurationSource source : sources()) {
            if (!(source instanceof DynamicConfigurationSource))
                continue;

            DynamicConfigurationSource dynamicSource = (DynamicConfigurationSource) source;
            dynamicSource.addSourceChangeListener(new ConfigurationSourceChangeListener() {

                @Override
                public void onChange(ConfigurationSourceChangeEvent e) {
                    onSourceChange(e);
                }

            });
        }

        _propertyCleanExecutorService.schedule(new Runnable() {
            @Override
            public void run() {
                cleanUselessListeners();
            }
        }, 60 * 1000, TimeUnit.MILLISECONDS);
    }

    @Override
    public DynamicProperty getProperty(String key) {
        return new DefaultDynamicProperty(this, super.getProperty(key));
    }

    @Override
    public synchronized void addPropertyChangeListener(PropertyChangeListener listener) {
        if (listener == null)
            return;

        globalPropertyChangeListeners().add(listener);
    }

    @Override
    public synchronized void addPropertyChangeListener(String key, PropertyChangeListener listener) {
        if (key == null || listener == null)
            return;

        List<PropertyChangeListener> listeners = propertyChangeListeners().get(key);
        if (listeners == null) {
            listeners = new CopyOnWriteArrayList<>();
            List<PropertyChangeListener> preValue = propertyChangeListeners().putIfAbsent(key, listeners);
            if (preValue != null)
                listeners = preValue;
        }
        listeners.add(listener);
    }

    protected ConfigurationSource getWorkingSourceForProperty(String key) {
        for (ConfigurationSource source : _sortedSources) {
            if (source.configuration().getPropertyValue(key) != null)
                return source;
        }

        return null;
    }

    protected void onSourceChange(ConfigurationSourceChangeEvent e) {
        if (CollectionValues.isNullOrEmpty(e.propertyChangeEvents()))
            return;

        for (PropertyChangeEvent propertyChangeEvent : e.propertyChangeEvents()) {
            String key = propertyChangeEvent.key();
            if (key == null)
                continue;

            ConfigurationSource source = getWorkingSourceForProperty(key);
            if (source != null && source.priority() > e.source().priority())
                continue;

            for (PropertyChangeListener listener : globalPropertyChangeListeners()) {
                handlePropertyChange(listener, propertyChangeEvent);
            }

            List<PropertyChangeListener> listeners = propertyChangeListeners().get(key);
            if (listeners == null)
                continue;

            for (PropertyChangeListener listener : listeners) {
                handlePropertyChange(listener, propertyChangeEvent);
            }
        }
    }

    protected void handlePropertyChange(final PropertyChangeListener listener, final PropertyChangeEvent e) {
        if (listener == null || e == null)
            return;

        _propertyChangeExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    listener.onChange(e);
                } catch (Throwable ex) {
                    _logger.error("PropertyChangeListener '" + listener.getClass() + "' throws an exception.", ex);
                }
            }
        });
    }

    private synchronized void cleanUselessListeners() {
        try {
            cleanUselessGlobalListeners();
            cleanUselessPropertyListeners();
        } catch (Throwable ex) {
            _logger.warn("Clean useless property change listeners failed.", ex);
        }
    }

    private void cleanUselessGlobalListeners() {
        cleanUselessListeners(globalPropertyChangeListeners());
    }

    private void cleanUselessPropertyListeners() {
        List<String> uselessPropertyKeys = new ArrayList<>();
        for (String propertyKey : propertyChangeListeners().keySet()) {
            List<PropertyChangeListener> listeners = propertyChangeListeners().get(propertyKey);
            cleanUselessListeners(listeners);
            if (CollectionValues.isNullOrEmpty(listeners))
                uselessPropertyKeys.add(propertyKey);
        }

        if (uselessPropertyKeys.isEmpty())
            return;

        for (String propertyKey : uselessPropertyKeys)
            propertyChangeListeners().remove(propertyKey);

        _logger.info(uselessPropertyKeys.size() + " useless property keys were cleaned.");
    }

    private void cleanUselessListeners(List<PropertyChangeListener> listeners) {
        try {
            if (CollectionValues.isNullOrEmpty(listeners))
                return;

            List<PropertyChangeListener> uselessPropertyChangeListeners = new ArrayList<>();
            for (PropertyChangeListener listener : listeners) {
                if (listener instanceof WeakReferencePropertyChangeListener<?>) {
                    if (((WeakReferencePropertyChangeListener<?>) listener).hasData())
                        continue;

                    uselessPropertyChangeListeners.add(listener);
                }
            }

            int uselessCount = uselessPropertyChangeListeners.size();
            if (uselessCount == 0)
                return;

            listeners.removeAll(uselessPropertyChangeListeners);
            _logger.info(uselessCount + " useless properties were cleaned.");
        } catch (Throwable ex) {
            _logger.warn("Clean useless property failed.", ex);
        }

    }
}