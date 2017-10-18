package com.ctrip.soa.caravan.configuration.typed;

import com.ctrip.soa.caravan.common.value.parser.ValueParser;
import com.ctrip.soa.caravan.configuration.ConfigurationManager;
import com.ctrip.soa.caravan.configuration.wrapper.ConfigurationManagerWrapper;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class DefaultTypedConfigurationManager extends ConfigurationManagerWrapper implements TypedConfigurationManager {

    public DefaultTypedConfigurationManager(ConfigurationManager manager) {
        super(manager);
    }

    @Override
    public <T> TypedProperty<T> getProperty(String key, ValueParser<T> valueParser) {
        return new DefaultTypedProperty<T>(getProperty(key), valueParser);
    }

}