package com.ctrip.soa.caravan.configuration.facade;

import org.junit.Assert;
import org.junit.Test;

import com.ctrip.soa.caravan.configuration.source.memory.MemoryConfigurationSource;
import com.ctrip.soa.caravan.configuration.typed.TypedProperty;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class TypedDynamicCachedCorrectedPropertiesTest {

    @Test
    public void propertiesCacheTest() {
        TypedDynamicCachedCorrectedProperties properties = new TypedDynamicCachedCorrectedProperties(new MemoryConfigurationSource(0, "memory"));

        TypedProperty<Boolean> uniqueProperty = properties.getBooleanProperty("unique");
        Assert.assertEquals(1, properties.typedProperties().size());

        TypedProperty<Integer> duplicateProperty1 = properties.getIntProperty("duplicate");
        TypedProperty<Integer> duplicateProperty2 = properties.getIntProperty("duplicate", 0);
        Assert.assertEquals(3, properties.typedProperties().size());

        TypedProperty<Integer> duplicateProperty3 = properties.getIntProperty("duplicate", -1, 1, 2);
        Assert.assertEquals(Integer.valueOf(0), duplicateProperty3.typedValue());
        Assert.assertEquals(duplicateProperty2, duplicateProperty3);
        Assert.assertEquals(3, properties.typedProperties().size());

        System.out.print(String.format("uniqueProperty: %s\n", uniqueProperty));
        System.out.print(String.format("duplicateProperty1: %s\n", duplicateProperty1));
        System.out.print(String.format("duplicateProperty2: %s\n", duplicateProperty2));
        System.out.print(String.format("duplicateProperty3: %s\n", duplicateProperty3));
    }

}
