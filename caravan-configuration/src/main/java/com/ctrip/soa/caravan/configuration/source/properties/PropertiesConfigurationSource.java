package com.ctrip.soa.caravan.configuration.source.properties;

import com.ctrip.soa.caravan.configuration.AbstractConfigurationSource;
import com.ctrip.soa.caravan.configuration.Configuration;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class PropertiesConfigurationSource extends AbstractConfigurationSource {

    private PropertiesConfiguration _configuration;

    public PropertiesConfigurationSource(final int priority, final String fileName) {
        super(priority, PropertiesConfiguration.getPropertiesFileName(fileName));
        _configuration = new PropertiesConfiguration(fileName);
    }

    @Override
    public Configuration configuration() {
        return _configuration;
    }
}