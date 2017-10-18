package com.ctrip.soa.caravan.configuration.typed.dynamic.cached.corrected;

import org.junit.Test;

import com.ctrip.soa.caravan.common.value.corrector.DefaultValueCorrector;
import com.ctrip.soa.caravan.common.value.corrector.ValueCorrector;
import com.ctrip.soa.caravan.common.value.parser.BooleanParser;
import com.ctrip.soa.caravan.configuration.facade.ConfigurationManagers;
import com.ctrip.soa.caravan.configuration.facade.ConfigurationSources;
import com.ctrip.soa.caravan.configuration.source.memory.MemoryConfigurationSource;
import com.ctrip.soa.caravan.configuration.typed.dynamic.cached.TypedDynamicCachedProperty;

/**
 * author: chennan
 */
public class DefaultTypedDynamicCachedCorrectedPropertyTest {

    private final static String TK = "soa.client.log.Exception.CService";

    private final static MemoryConfigurationSource _globalConfigurationSource = ConfigurationSources.newMemorySource(1, "GlobalConfigurationSource");

    private final MemoryConfigurationSource _instanceConfigurationSource = ConfigurationSources.newMemorySource(2, "InstanceConfigurationSource");

    private final TypedDynamicCachedCorrectedConfigurationManager _manager = ConfigurationManagers.newTypedDynamicCachedCorrectedManager(_globalConfigurationSource, _instanceConfigurationSource);

    @Test
    public void testBooleanType() {
        TypedDynamicCachedCorrectedProperty<Boolean> property = _manager.getProperty(TK, BooleanParser.DEFAULT, corrector(true));
        _globalConfigurationSource.configuration().setPropertyValue(TK, "false");
        System.out.println(property.typedValue());

        TypedDynamicCachedProperty<Boolean> property2 = _manager.getProperty(TK, BooleanParser.DEFAULT);
        _globalConfigurationSource.configuration().setPropertyValue(TK, "false");
        System.out.println(property2.typedValue());
    }

    private <T> ValueCorrector<T> corrector(T defaultValue) {
        return new DefaultValueCorrector<>(defaultValue);
    }
}