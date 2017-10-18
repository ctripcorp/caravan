package com.ctrip.soa.caravan.ribbon;

import com.ctrip.soa.caravan.configuration.facade.ConfigurationManagers;
import com.ctrip.soa.caravan.configuration.facade.TypedDynamicCachedCorrectedProperties;
import com.ctrip.soa.caravan.configuration.source.properties.PropertiesConfigurationSource;
import com.ctrip.soa.caravan.configuration.typed.dynamic.cached.corrected.TypedDynamicCachedCorrectedConfigurationManager;

/**
 * Created by w.jian on 2016/7/19.
 */
public class TestUtils {

    public static TypedDynamicCachedCorrectedProperties getProperties() {
        PropertiesConfigurationSource propertiesConfigurationSource = new PropertiesConfigurationSource(0, "soa");
        TypedDynamicCachedCorrectedConfigurationManager manager;
        manager = ConfigurationManagers.newTypedDynamicCachedCorrectedManager(propertiesConfigurationSource);
        return new TypedDynamicCachedCorrectedProperties(manager);
    }

    public static LoadBalancerManagerConfig getLoadBalancerManagerConfig() {
        return new LoadBalancerManagerConfig(getProperties());
    }
}
