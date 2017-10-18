package com.ctrip.soa.caravan.util.safelist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ctrip.soa.caravan.common.value.parser.ValueParser;
import com.ctrip.soa.caravan.configuration.facade.TypedDynamicCachedCorrectedProperties;
import com.ctrip.soa.caravan.configuration.typed.TypedProperty;
import com.ctrip.soa.caravan.configuration.util.PropertyKeyGenerator;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
class DefaultSafeList<T> implements SafeList<T> {

    private static final Logger _logger = LoggerFactory.getLogger(DefaultSafeList.class);

    private String _safeListId;
    private SafeListChecker<T> _checker;

    private TypedProperty<Boolean> _enabledProperty;
    private TypedProperty<List<T>> _listProperty;

    public DefaultSafeList(String safeListId, ValueParser<List<T>> valueParser, TypedDynamicCachedCorrectedProperties properties, SafeListConfig<T> config) {
        _safeListId = safeListId;
        _checker = config.checker();

        String propertyKey = PropertyKeyGenerator.generateKey(_safeListId, SafeListConfig.ENABLED_PROPERTY_KEY);
        _enabledProperty = properties.getBooleanProperty(propertyKey, config.enabled());

        propertyKey = PropertyKeyGenerator.generateKey(_safeListId, SafeListConfig.LIST_PROPERTY_KEY);
        _listProperty = properties.getProperty(propertyKey, valueParser, config.list() == null ? new ArrayList<T>() : config.list());
    }

    @Override
    public String safeListId() {
        return _safeListId;
    }

    @Override
    public List<T> list() {
        return Collections.unmodifiableList(_listProperty.typedValue());
    }

    @Override
    public boolean check(T item) {
        if (!_enabledProperty.typedValue())
            return true;

        try {
            return _checker.check(_listProperty.typedValue(), item);
        } catch (Throwable ex) {
            _logger.error("Safe list failed to check.", ex);
            return true;
        }
    }

}
