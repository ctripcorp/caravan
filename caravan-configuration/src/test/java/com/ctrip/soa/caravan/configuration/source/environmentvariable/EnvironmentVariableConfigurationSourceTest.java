package com.ctrip.soa.caravan.configuration.source.environmentvariable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.common.base.Strings;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class EnvironmentVariableConfigurationSourceTest {

    @Test
    public void testEnvironmentVariableConfigurationSource() {
        EnvironmentVariableConfigurationSource cs = new EnvironmentVariableConfigurationSource(0);
        assertEquals(cs.priority(), 0);
    }

    @Test
    public void testEnvironmentVariableConfigurationSourceInt() {
        EnvironmentVariableConfigurationSource cs = new EnvironmentVariableConfigurationSource(10);
        assertEquals(cs.priority(), 10);
    }

    @Test
    public void testPriority() {
        EnvironmentVariableConfigurationSource cs1 = new EnvironmentVariableConfigurationSource(1);
        assertEquals(cs1.priority(), 1);
        EnvironmentVariableConfigurationSource cs2 = new EnvironmentVariableConfigurationSource(0);
        assertEquals(cs2.priority(), 0);
        EnvironmentVariableConfigurationSource cs3 = new EnvironmentVariableConfigurationSource(-1);
        assertEquals(cs3.priority(), -1);
    }

    @Test
    public void testConfiguration() {
        EnvironmentVariableConfigurationSource cs = new EnvironmentVariableConfigurationSource(0);
        assertNotNull(cs.configuration());
        assertTrue(!Strings.isNullOrEmpty(cs.configuration().getPropertyValue("sun.management.compiler")));
    }
}