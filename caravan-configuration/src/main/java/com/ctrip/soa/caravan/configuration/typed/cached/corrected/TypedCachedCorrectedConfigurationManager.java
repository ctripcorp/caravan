package com.ctrip.soa.caravan.configuration.typed.cached.corrected;

import com.ctrip.soa.caravan.common.value.corrector.ValueCorrector;
import com.ctrip.soa.caravan.common.value.parser.ValueParser;
import com.ctrip.soa.caravan.configuration.typed.cached.TypedCachedConfigurationManager;
import com.ctrip.soa.caravan.configuration.typed.corrected.TypedCorrectedConfigurationManager;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public interface TypedCachedCorrectedConfigurationManager
        extends TypedCachedConfigurationManager, TypedCorrectedConfigurationManager {

    <T> TypedCachedCorrectedProperty<T> getProperty(String key, ValueParser<T> valueParser, ValueCorrector<T> valueCorrector);

}
