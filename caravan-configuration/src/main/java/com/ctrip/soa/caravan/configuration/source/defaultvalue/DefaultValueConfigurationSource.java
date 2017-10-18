package com.ctrip.soa.caravan.configuration.source.defaultvalue;

import java.util.Map;

import com.ctrip.soa.caravan.configuration.AbstractConfigurationSource;

/**
 * Created by w.jian on 2016/5/17.
 */
public final class DefaultValueConfigurationSource extends AbstractConfigurationSource {

    private final DefaultValueConfiguration _defaultValueConfiguration;

    public DefaultValueConfigurationSource(Map<String, String> keyValueMap) {
        super(Integer.MIN_VALUE, "DefaultValue");
        _defaultValueConfiguration = new DefaultValueConfiguration(keyValueMap);
    }

    @Override
    public DefaultValueConfiguration configuration() {
        return _defaultValueConfiguration;
    }

}
