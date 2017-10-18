package com.ctrip.soa.caravan.util.serializer.filterable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.ctrip.soa.caravan.common.value.checker.StringArgumentChecker;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class FilterableProperty {

    private String _propertyName;
    private ConcurrentHashMap<String, String> _metadata;
    private volatile SerializationFilter _serializationFilter;

    public FilterableProperty(String propertyName) {
        StringArgumentChecker.DEFAULT.check(propertyName, "propertyName");
        _propertyName = propertyName;
        _metadata = new ConcurrentHashMap<>();
    }

    public String name() {
        return _propertyName;
    }

    public Map<String, String> metadata() {
        return _metadata;
    }

    public SerializationFilter getSerializationFilter() {
        return _serializationFilter;
    }

    public void setSerializationFilter(SerializationFilter serializationFilter) {
        _serializationFilter = serializationFilter;
    }

}
