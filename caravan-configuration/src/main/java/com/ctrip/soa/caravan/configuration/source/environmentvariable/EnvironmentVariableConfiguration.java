package com.ctrip.soa.caravan.configuration.source.environmentvariable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ctrip.soa.caravan.configuration.Configuration;
import com.google.common.base.Strings;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class EnvironmentVariableConfiguration implements Configuration {

    private static final Logger _logger = LoggerFactory.getLogger(EnvironmentVariableConfiguration.class);

    @Override
    public String getPropertyValue(String key) {
        if (Strings.isNullOrEmpty(key)) {
            _logger.warn("ENV key is null or empty!");
            return null;
        }
        String value = System.getProperty(key);
        return value;
    }
}