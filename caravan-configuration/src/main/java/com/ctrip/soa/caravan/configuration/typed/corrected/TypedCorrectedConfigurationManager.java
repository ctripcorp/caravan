package com.ctrip.soa.caravan.configuration.typed.corrected;

import com.ctrip.soa.caravan.common.value.corrector.ValueCorrector;
import com.ctrip.soa.caravan.common.value.parser.ValueParser;
import com.ctrip.soa.caravan.configuration.corrected.CorrectedConfigurationManager;
import com.ctrip.soa.caravan.configuration.typed.TypedConfigurationManager;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public interface TypedCorrectedConfigurationManager extends TypedConfigurationManager, CorrectedConfigurationManager {

    <T> TypedCorrectedProperty<T> getProperty(String key, ValueParser<T> valueParser, ValueCorrector<T> valueCorrector);

}