package com.ctrip.soa.caravan.configuration.typed.cached;

import com.ctrip.soa.caravan.common.value.parser.ValueParser;
import com.ctrip.soa.caravan.configuration.cached.CachedConfigurationManager;
import com.ctrip.soa.caravan.configuration.cached.CachedProperty;
import com.ctrip.soa.caravan.configuration.typed.DefaultTypedConfigurationManager;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class DefaultTypedCachedConfigurationManager extends DefaultTypedConfigurationManager
        implements TypedCachedConfigurationManager {

    public DefaultTypedCachedConfigurationManager(CachedConfigurationManager manager) {
        super(manager);
    }

    @Override
    protected CachedConfigurationManager manager() {
        return (CachedConfigurationManager) super.manager();
    }

    @Override
    public <T> TypedCachedProperty<T> getProperty(String key, ValueParser<T> valueParser) {
        return new DefaultTypedCachedProperty<T>(getProperty(key), valueParser);
    }

    @Override
    public CachedProperty getProperty(String key) {
        return manager().getProperty(key);
    }

}
