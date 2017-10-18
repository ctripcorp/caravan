package com.ctrip.soa.caravan.configuration.typed.dynamic.cached;

import com.ctrip.soa.caravan.common.value.parser.ValueParser;
import com.ctrip.soa.caravan.configuration.dynamic.cached.DynamicCachedConfigurationManager;
import com.ctrip.soa.caravan.configuration.dynamic.cached.DynamicCachedProperty;
import com.ctrip.soa.caravan.configuration.typed.dynamic.DefaultTypedDynamicConfigurationManager;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class DefaultTypedDynamicCachedConfigurationManager extends DefaultTypedDynamicConfigurationManager implements TypedDynamicCachedConfigurationManager {

    public DefaultTypedDynamicCachedConfigurationManager(DynamicCachedConfigurationManager manager) {
        super(manager);
    }
    
    @Override
    protected DynamicCachedConfigurationManager manager() {
        return (DynamicCachedConfigurationManager)super.manager();
    }
    
    @Override
    public DynamicCachedProperty getProperty(String key) {
        return manager().getProperty(key);
    }

    @Override
    public <T> TypedDynamicCachedProperty<T> getProperty(String key, ValueParser<T> valueParser) {
        return new DefaultTypedDynamicCachedProperty<T>(getProperty(key), valueParser);
    }

}
