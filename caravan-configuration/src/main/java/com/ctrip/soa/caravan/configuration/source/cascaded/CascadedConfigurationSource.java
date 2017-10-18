package com.ctrip.soa.caravan.configuration.source.cascaded;

import java.util.List;

import com.ctrip.soa.caravan.common.value.checker.NullArgumentChecker;
import com.ctrip.soa.caravan.configuration.AbstractConfigurationSource;
import com.ctrip.soa.caravan.configuration.Configuration;
import com.ctrip.soa.caravan.configuration.ConfigurationSource;
import com.google.common.collect.Lists;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class CascadedConfigurationSource extends AbstractConfigurationSource {

    private CascadedConfiguration _configuration;

    public CascadedConfigurationSource(ConfigurationSource source, String... cascadedFactors) {
        this(source, CascadedConfiguration.DEFAULT_KEY_SEPARATOR, Lists.newArrayList(cascadedFactors));
    }

    public CascadedConfigurationSource(ConfigurationSource source, String keySperator, List<String> cascadedFactors) {
        super(source.priority(), source.sourceId());

        NullArgumentChecker.DEFAULT.check(source, "source");
        _configuration = new CascadedConfiguration(source.configuration(), keySperator, cascadedFactors);
    }

    @Override
    public Configuration configuration() {
        return _configuration;
    }

    protected CascadedConfiguration cascadedConfiguration() {
        return _configuration;
    }

}
