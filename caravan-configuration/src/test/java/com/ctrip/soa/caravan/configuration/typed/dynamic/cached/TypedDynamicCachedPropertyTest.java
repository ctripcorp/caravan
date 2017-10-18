package com.ctrip.soa.caravan.configuration.typed.dynamic.cached;

import java.util.List;

import com.ctrip.soa.caravan.configuration.ConfigurationSource;
import com.ctrip.soa.caravan.configuration.facade.TypedDynamicCachedCorrectedProperties;
import com.ctrip.soa.caravan.configuration.source.defaultvalue.DefaultValueConfigurationSource;
import com.ctrip.soa.caravan.configuration.typed.TypedProperty;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

/**
 * author: chennan
 */
public class TypedDynamicCachedPropertyTest {

    static List<ConfigurationSource> sources = Lists.newArrayList();

    public static TypedDynamicCachedCorrectedProperties properties;

    static {
        sources.add(new DefaultValueConfigurationSource(ImmutableMap.of("key", "right")));
        properties = new TypedDynamicCachedCorrectedProperties(sources);
    }

    public static void main(String[] args) {
        TypedProperty<String> property1 = TypedDynamicCachedPropertyTest.properties.getStringProperty("key");
        TypedProperty<String> property2 = TypedDynamicCachedPropertyTest.properties.getStringProperty("key", "wrong");
        System.err.println(property1.typedValue());
        System.err.println(property2.typedValue());
    }

}
