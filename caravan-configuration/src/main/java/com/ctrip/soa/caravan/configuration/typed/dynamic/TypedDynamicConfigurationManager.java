package com.ctrip.soa.caravan.configuration.typed.dynamic;

import com.ctrip.soa.caravan.common.value.parser.ValueParser;
import com.ctrip.soa.caravan.configuration.dynamic.DynamicConfigurationManager;
import com.ctrip.soa.caravan.configuration.typed.TypedConfigurationManager;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public interface TypedDynamicConfigurationManager extends TypedConfigurationManager, DynamicConfigurationManager {
    
    <T> TypedDynamicProperty<T> getProperty(String key, ValueParser<T> valueParser);

}
