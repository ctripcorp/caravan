package com.ctrip.soa.caravan.configuration.source.properties;

import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ctrip.soa.caravan.common.value.StringValues;
import com.ctrip.soa.caravan.common.value.checker.StringArgumentChecker;
import com.ctrip.soa.caravan.configuration.Configuration;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class PropertiesConfiguration implements Configuration {

    private static final Logger _logger = LoggerFactory.getLogger(PropertiesConfiguration.class);

    private static final String PROPERTIES_FILE_EXTENSION = ".properties";

    private Properties prop = new Properties();

    public PropertiesConfiguration(String fileName) {
        fileName = getPropertiesFileName(fileName);
        try {
            try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName)) {
                if (is == null) {
                    _logger.warn("No file {} exists.", fileName);
                    return;
                }
                prop.load(is);
            }
        } catch (Throwable ex) {
            _logger.error(String.format("Exception occured when load File: ", fileName), ex);
        }
    }

    @Override
    public String getPropertyValue(String key) {
        if (key == null)
            return null;

        String value = prop.getProperty(key);
        return StringValues.isNullOrWhitespace(value) ? null : value.trim();
    }

    static String getPropertiesFileName(String fileName) {
        StringArgumentChecker.DEFAULT.check(fileName, "fileName");

        if (fileName.endsWith(PROPERTIES_FILE_EXTENSION))
            return fileName;
        return fileName + PROPERTIES_FILE_EXTENSION;
    }
}