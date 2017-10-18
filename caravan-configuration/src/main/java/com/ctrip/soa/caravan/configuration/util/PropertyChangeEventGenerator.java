package com.ctrip.soa.caravan.configuration.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ctrip.soa.caravan.common.value.MapValues;
import com.ctrip.soa.caravan.common.value.StringValues;
import com.ctrip.soa.caravan.configuration.dynamic.DefaultPropertyChangeEvent;
import com.ctrip.soa.caravan.configuration.dynamic.PropertyChangeEvent;
import com.google.common.base.Objects;
import com.google.common.collect.Sets;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class PropertyChangeEventGenerator {
    public static List<PropertyChangeEvent> generatePropertyChangeEvents(Map<String, String> oldMap,
            Map<String, String> newMap) {
        if (MapValues.isNullOrEmpty(oldMap)) {
            oldMap = new HashMap<String, String>();
        }
        if (MapValues.isNullOrEmpty(newMap)) {
            newMap = new HashMap<String, String>();
        }
        List<PropertyChangeEvent> propertyChangeEvents = new ArrayList<>();

        for (String key : oldMap.keySet()) {
            String oldValue = StringValues.trim(oldMap.get(key));
            String newValue = StringValues.trim(newMap.get(key));
            if (Objects.equal(oldValue, newValue))
                continue;

            PropertyChangeEvent event = new DefaultPropertyChangeEvent(key, oldValue, newValue,
                    System.currentTimeMillis());
            propertyChangeEvents.add(event);
        }

        for (String key : Sets.difference(newMap.keySet(), oldMap.keySet())) {
            String newValue = StringValues.trim(newMap.get(key));
            if (StringValues.isNullOrWhitespace(newValue))
                continue;

            PropertyChangeEvent event = new DefaultPropertyChangeEvent(key, null, newValue, System.currentTimeMillis());
            propertyChangeEvents.add(event);
        }

        return propertyChangeEvents;
    }
}
