package com.ctrip.soa.caravan.configuration.source.defaultvalue;

import com.ctrip.soa.caravan.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.HashMap;

/**
 * Created by w.jian on 2016/5/17.
 */
public final class DefaultValueConfiguration implements Configuration {

    protected static final Logger _logger = LoggerFactory.getLogger(DefaultValueConfiguration.class);

    private final HashMap<String, String> _configurationMap;

    public DefaultValueConfiguration(Map<String, String> keyValueMap) {
        _configurationMap = new HashMap<>();

        if (keyValueMap == null)
            return;

        for (String key : keyValueMap.keySet()) {
            String value = keyValueMap.get(key);
            if (value == null)
                continue;

            _configurationMap.put(key, value);
        }
    }

    @Override
    public String getPropertyValue(String key) {
        if (key == null)
            return null;

        return _configurationMap.get(key);
    }

}
