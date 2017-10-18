package com.ctrip.soa.caravan.configuration.typed.dynamic.cached.corrected;

import com.ctrip.soa.caravan.common.value.corrector.ValueCorrector;
import com.ctrip.soa.caravan.common.value.parser.ValueParser;
import com.ctrip.soa.caravan.configuration.dynamic.cached.corrected.DynamicCachedCorrectedConfigurationManager;
import com.ctrip.soa.caravan.configuration.typed.dynamic.cached.TypedDynamicCachedConfigurationManager;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public interface TypedDynamicCachedCorrectedConfigurationManager
        extends TypedDynamicCachedConfigurationManager, DynamicCachedCorrectedConfigurationManager {

    <T> TypedDynamicCachedCorrectedProperty<T> getProperty(String key, ValueParser<T> valueParser,
            ValueCorrector<T> valueCorrector);

}
