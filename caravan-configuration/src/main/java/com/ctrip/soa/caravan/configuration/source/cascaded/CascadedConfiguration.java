package com.ctrip.soa.caravan.configuration.source.cascaded;

import java.util.Collections;
import java.util.List;

import com.ctrip.soa.caravan.common.value.StringValues;
import com.ctrip.soa.caravan.common.value.checker.NullArgumentChecker;
import com.ctrip.soa.caravan.common.value.checker.StringArgumentChecker;
import com.ctrip.soa.caravan.configuration.Configuration;
import com.google.common.collect.Lists;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class CascadedConfiguration implements Configuration {

    public static final String DEFAULT_KEY_SEPARATOR = ".";

    private Configuration _configuration;
    private String _keySeparator;
    private List<String> _cascadedKeyParts;

    public CascadedConfiguration(Configuration configuration, String... cascadedFactors) {
        this(configuration, DEFAULT_KEY_SEPARATOR, Lists.newArrayList(cascadedFactors));
    }

    public CascadedConfiguration(Configuration configuration, String keySeparator, List<String> cascadedFactors) {
        NullArgumentChecker.DEFAULT.check(configuration, "configuration");
        StringArgumentChecker.DEFAULT.check(keySeparator, "keySeperator");
        NullArgumentChecker.DEFAULT.check(cascadedFactors, "cascadedFactors");

        _configuration = configuration;
        _keySeparator = keySeparator.trim();
        _cascadedKeyParts = Lists.newArrayList();

        StringBuffer keyPart = new StringBuffer(StringValues.EMPTY);
        _cascadedKeyParts.add(keyPart.toString());
        for (String factor : cascadedFactors) {
            if (StringValues.isNullOrWhitespace(factor))
                continue;
            keyPart.append(_keySeparator).append(factor);
            _cascadedKeyParts.add(keyPart.toString());
        }

        Collections.reverse(_cascadedKeyParts);
        _cascadedKeyParts = Collections.unmodifiableList(_cascadedKeyParts);
    }

    @Override
    public String getPropertyValue(String key) {
        for (String keyPart : _cascadedKeyParts) {
            String value = _configuration.getPropertyValue(key + keyPart);
            if (value != null)
                return value;
        }

        return null;
    }

    public String keySeparator() {
        return _keySeparator;
    }

    public List<String> cascadedKeyParts() {
        return _cascadedKeyParts;
    }

    public Configuration configuration() {
        return _configuration;
    }

}
