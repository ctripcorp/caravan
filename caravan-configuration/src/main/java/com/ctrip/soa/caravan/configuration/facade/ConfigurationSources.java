package com.ctrip.soa.caravan.configuration.facade;

import java.util.Map;

import com.ctrip.soa.caravan.configuration.ConfigurationSource;
import com.ctrip.soa.caravan.configuration.dynamic.DynamicConfigurationSource;
import com.ctrip.soa.caravan.configuration.source.cascaded.CascadedConfigurationSource;
import com.ctrip.soa.caravan.configuration.source.cascaded.dynamic.CascadedDynamicConfigurationSource;
import com.ctrip.soa.caravan.configuration.source.defaultvalue.DefaultValueConfigurationSource;
import com.ctrip.soa.caravan.configuration.source.memory.MemoryConfigurationSource;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class ConfigurationSources {

    protected ConfigurationSources() {
    }

    public static DefaultValueConfigurationSource newDefaultValueSource(Map<String, String> keyValueMap) {
        return new DefaultValueConfigurationSource(keyValueMap);
    }

    public static MemoryConfigurationSource newMemorySource(int priority, String sourceId) {
        return new MemoryConfigurationSource(priority, sourceId);
    }

    public static CascadedConfigurationSource newCascadedSource(ConfigurationSource source, String... cascadedFactors) {
        return new CascadedConfigurationSource(source, cascadedFactors);
    }

    public static CascadedDynamicConfigurationSource newCascadedDynamicSource(DynamicConfigurationSource source,
            String... cascadedFactors) {
        return new CascadedDynamicConfigurationSource(source, cascadedFactors);
    }

}
