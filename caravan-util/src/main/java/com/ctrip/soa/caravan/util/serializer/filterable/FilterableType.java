package com.ctrip.soa.caravan.util.serializer.filterable;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.ctrip.soa.caravan.common.value.CollectionValues;
import com.ctrip.soa.caravan.common.value.StringValues;
import com.ctrip.soa.caravan.common.value.checker.NullArgumentChecker;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class FilterableType {

    private Class<?> _type;
    private ConcurrentHashMap<String, FilterableProperty> _properties = new ConcurrentHashMap<>();

    public FilterableType(Class<?> type) {
        NullArgumentChecker.DEFAULT.check(type, "type");
        _type = type;
    }

    public Class<?> type() {
        return _type;
    }

    public Collection<FilterableProperty> properties() {
        return _properties.values();
    }

    public void addProperties(List<FilterableProperty> properties) {
        if (CollectionValues.isNullOrEmpty(properties))
            return;

        for (FilterableProperty property : properties)
            addProperty(property);
    }

    public void addProperty(FilterableProperty property) {
        if (property == null)
            return;

        _properties.put(property.name().toLowerCase(), property);
    }

    public FilterableProperty getProperty(String propertyName) {
        if (StringValues.isNullOrWhitespace(propertyName))
            return null;

        propertyName = propertyName.trim().toLowerCase();
        return _properties.get(propertyName);
    }

}
