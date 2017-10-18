package com.ctrip.soa.caravan.configuration.cached;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ctrip.soa.caravan.configuration.Property;
import com.ctrip.soa.caravan.configuration.wrapper.PropertyWrapper;
import com.google.common.base.Objects;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class DefaultCachedProperty extends PropertyWrapper implements CachedProperty {

    private static final Logger _logger = LoggerFactory.getLogger(DefaultCachedProperty.class);

    private static boolean _disableRefreshLog;

    static {
        String disableRefreshLog = System.getProperty("caravan.config.cache-refresh-log.disabled", "false");
        _disableRefreshLog = Boolean.TRUE.toString().equalsIgnoreCase(disableRefreshLog);
    }

    private volatile String _value;

    public DefaultCachedProperty(Property property) {
        super(property);
        refresh();
    }

    @Override
    public String value() {
        return _value;
    }

    @Override
    public void refresh() {
        String before = value();
        _value = super.value();

        if (Objects.equal(before, _value))
            return;

        if (_disableRefreshLog)
            return;

        _logger.info(
                String.format("Default cached property has been refreshed, key: %s, the value before refresh: %s, after refresh: %s", key(), before, _value));
    }
}