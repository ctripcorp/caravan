package com.ctrip.soa.caravan.configuration.typed.dynamic.corrected;

import com.ctrip.soa.caravan.common.value.corrector.ValueCorrector;
import com.ctrip.soa.caravan.common.value.parser.ValueParser;
import com.ctrip.soa.caravan.configuration.dynamic.corrected.DynamicCorrectedConfigurationManager;
import com.ctrip.soa.caravan.configuration.typed.dynamic.TypedDynamicConfigurationManager;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public interface TypedDynamicCorrectedConfigurationManager
        extends TypedDynamicConfigurationManager, DynamicCorrectedConfigurationManager {

    <T> TypedDynamicCorrectedProperty<T> getProperty(String key, ValueParser<T> valueParser,
            ValueCorrector<T> valueCorrector);

}
