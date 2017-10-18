package com.ctrip.soa.caravan.configuration.source.environmentvariable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class EnvironmentVariableConfigurationTest {

    @Test
    public void testGetPropertyValue() {
        EnvironmentVariableConfiguration configuration = new EnvironmentVariableConfiguration();
        assertNull(configuration.getPropertyValue(null));
        assertNull(configuration.getPropertyValue(""));
        assertNull(configuration.getPropertyValue("test"));
        assertEquals("HotSpot 64-Bit Tiered Compilers", configuration.getPropertyValue("sun.management.compiler"));
    }
}