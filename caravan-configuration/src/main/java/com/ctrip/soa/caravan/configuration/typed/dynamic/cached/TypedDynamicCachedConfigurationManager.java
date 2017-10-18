package com.ctrip.soa.caravan.configuration.typed.dynamic.cached;

import com.ctrip.soa.caravan.common.value.parser.ValueParser;
import com.ctrip.soa.caravan.configuration.dynamic.cached.DynamicCachedConfigurationManager;
import com.ctrip.soa.caravan.configuration.typed.dynamic.TypedDynamicConfigurationManager;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public interface TypedDynamicCachedConfigurationManager extends TypedDynamicConfigurationManager, DynamicCachedConfigurationManager {
    
    <T> TypedDynamicCachedProperty<T> getProperty(String key, ValueParser<T> valueParser);

}
