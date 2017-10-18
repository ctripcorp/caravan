package com.ctrip.soa.caravan.configuration.typed.cached;

import com.ctrip.soa.caravan.common.value.parser.ValueParser;
import com.ctrip.soa.caravan.configuration.cached.CachedConfigurationManager;
import com.ctrip.soa.caravan.configuration.typed.TypedConfigurationManager;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public interface TypedCachedConfigurationManager extends TypedConfigurationManager, CachedConfigurationManager {

    <T> TypedCachedProperty<T> getProperty(String key, ValueParser<T> valueParser);

}
