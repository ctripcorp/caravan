package com.ctrip.soa.caravan.configuration.source.environmentvariable;

import com.ctrip.soa.caravan.configuration.AbstractConfigurationSource;
import com.ctrip.soa.caravan.configuration.Configuration;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class EnvironmentVariableConfigurationSource extends AbstractConfigurationSource {

    private EnvironmentVariableConfiguration _configuration = new EnvironmentVariableConfiguration();

    public EnvironmentVariableConfigurationSource(final int priority) {
        super(priority, "EnvironmentVariable");
    }

    @Override
    public Configuration configuration() {
        return _configuration;
    }

}