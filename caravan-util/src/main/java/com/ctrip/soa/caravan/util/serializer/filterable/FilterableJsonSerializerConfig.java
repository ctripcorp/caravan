package com.ctrip.soa.caravan.util.serializer.filterable;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class FilterableJsonSerializerConfig {

    private ConcurrentHashMap<Class<?>, FilterableType> _filterableTypes = new ConcurrentHashMap<>();

    public FilterableJsonSerializerConfig() {

    }

    public Collection<FilterableType> filterableTypes() {
        return _filterableTypes.values();
    }

    public FilterableType getFilterableType(Class<?> type) {
        return _filterableTypes.get(type);
    }

    public void addFilterableTypes(List<FilterableType> filterableTypes) {
        if (filterableTypes == null)
            return;

        for (FilterableType filterableType : filterableTypes)
            addFilterableType(filterableType);
    }

    public void addFilterableType(FilterableType filterableType) {
        if (filterableType == null)
            return;

        if (filterableType.type() == null)
            return;

        _filterableTypes.put(filterableType.type(), filterableType);
    }

}
