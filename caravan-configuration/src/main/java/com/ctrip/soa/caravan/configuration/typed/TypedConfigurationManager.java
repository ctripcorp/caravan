package com.ctrip.soa.caravan.configuration.typed;

import com.ctrip.soa.caravan.common.value.parser.ValueParser;
import com.ctrip.soa.caravan.configuration.ConfigurationManager;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public interface TypedConfigurationManager extends ConfigurationManager {

    <T> TypedProperty<T> getProperty(String key, ValueParser<T> valueParser);

}