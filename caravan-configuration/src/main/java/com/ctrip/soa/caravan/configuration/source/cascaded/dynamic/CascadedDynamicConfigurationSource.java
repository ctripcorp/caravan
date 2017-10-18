package com.ctrip.soa.caravan.configuration.source.cascaded.dynamic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ctrip.soa.caravan.common.value.StringValues;
import com.ctrip.soa.caravan.common.value.checker.NullArgumentChecker;
import com.ctrip.soa.caravan.configuration.dynamic.ConfigurationSourceChangeEvent;
import com.ctrip.soa.caravan.configuration.dynamic.ConfigurationSourceChangeListener;
import com.ctrip.soa.caravan.configuration.dynamic.DefaultConfigurationSourceChangeEvent;
import com.ctrip.soa.caravan.configuration.dynamic.DefaultPropertyChangeEvent;
import com.ctrip.soa.caravan.configuration.dynamic.DynamicConfigurationSource;
import com.ctrip.soa.caravan.configuration.dynamic.PropertyChangeEvent;
import com.ctrip.soa.caravan.configuration.source.cascaded.CascadedConfiguration;
import com.ctrip.soa.caravan.configuration.source.cascaded.CascadedConfigurationSource;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class CascadedDynamicConfigurationSource extends CascadedConfigurationSource implements DynamicConfigurationSource {

    private DynamicConfigurationSource _source;

    public CascadedDynamicConfigurationSource(DynamicConfigurationSource source, String... cascadedFactors) {
        this(source, CascadedConfiguration.DEFAULT_KEY_SEPARATOR, Lists.newArrayList(cascadedFactors));
    }

    public CascadedDynamicConfigurationSource(DynamicConfigurationSource source, String keySperator, List<String> cascadedFactors) {
        super(source, keySperator, cascadedFactors);
        _source = source;
    }

    @Override
    public void addSourceChangeListener(final ConfigurationSourceChangeListener listener) {
        NullArgumentChecker.DEFAULT.check(listener, "listener");

        _source.addSourceChangeListener(new ConfigurationSourceChangeListener() {

            @Override
            public void onChange(ConfigurationSourceChangeEvent event) {
                ListMultimap<String, PropertyChangeEvent> propertyChangeEventsMultiMap = getRealPropertyChangeEventsMultiMap(event);
                List<PropertyChangeEvent> realPropertyChangeEvents = new ArrayList<>();
                for (String key : propertyChangeEventsMultiMap.keySet()) {
                    PropertyChangeEvent realPropertyChangeEvent = getRealPropertyChangeEvent(key, propertyChangeEventsMultiMap.get(key));
                    realPropertyChangeEvents.add(realPropertyChangeEvent);
                }

                DefaultConfigurationSourceChangeEvent newSourceChangeEvent = new DefaultConfigurationSourceChangeEvent(event.source(), realPropertyChangeEvents,
                        event.changedTimeInMs());
                listener.onChange(newSourceChangeEvent);
            }

        });
    }

    private ListMultimap<String, PropertyChangeEvent> getRealPropertyChangeEventsMultiMap(ConfigurationSourceChangeEvent event) {
        ListMultimap<String, PropertyChangeEvent> propertyChangeEventsMultiMap = ArrayListMultimap.create();
        for (PropertyChangeEvent propertyChangeEvent : event.propertyChangeEvents()) {
            String key = propertyChangeEvent.key();
            String realKey = null;
            for (String cascadedKeyPart : cascadedConfiguration().cascadedKeyParts()) {
                if (realKey == null) {
                    String cascadedKey = StringValues.trimEnd(key, cascadedKeyPart);
                    if (!cascadedKey.equals(key))
                        realKey = cascadedKey;
                }
                if (realKey != null)
                    propertyChangeEventsMultiMap.put(realKey + cascadedKeyPart, propertyChangeEvent);
            }
            if (realKey == null)
                propertyChangeEventsMultiMap.put(key, propertyChangeEvent);
        }
        return propertyChangeEventsMultiMap;
    }

    private PropertyChangeEvent getRealPropertyChangeEvent(String realKey, List<PropertyChangeEvent> propertyChangeEvents) {
        Map<String, PropertyChangeEvent> changedKeyPartEventMap = new HashMap<>();
        for (PropertyChangeEvent propertyChangeEvent : propertyChangeEvents) {
            changedKeyPartEventMap.put(StringValues.trimStart(propertyChangeEvent.key(), realKey), propertyChangeEvent);
        }

        String newValue = null;
        String oldValue = null;
        long changedTime = 0;
        for (String keyPart : cascadedConfiguration().cascadedKeyParts()) {
            if (changedKeyPartEventMap.keySet().contains(keyPart)) {
                PropertyChangeEvent propertyChangeEvent = changedKeyPartEventMap.get(keyPart);
                if (changedTime == 0)
                    changedTime = propertyChangeEvent.changedTimeInMs();

                if (newValue == null && propertyChangeEvent.newValue() != null)
                    newValue = propertyChangeEvent.newValue();

                if (oldValue == null && propertyChangeEvent.oldValue() != null)
                    oldValue = propertyChangeEvent.oldValue();

                continue;
            }

            if (oldValue == null)
                oldValue = cascadedConfiguration().configuration().getPropertyValue(realKey + keyPart);
        }

        return new DefaultPropertyChangeEvent(realKey, oldValue, newValue, changedTime);
    }

}
