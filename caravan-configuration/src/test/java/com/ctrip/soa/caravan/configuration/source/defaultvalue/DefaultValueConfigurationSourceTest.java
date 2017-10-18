package com.ctrip.soa.caravan.configuration.source.defaultvalue;

import org.junit.Assert;
import org.junit.Test;

import com.ctrip.soa.caravan.configuration.ConfigurationManager;
import com.ctrip.soa.caravan.configuration.facade.ConfigurationManagers;
import com.ctrip.soa.caravan.configuration.facade.ConfigurationSources;
import com.ctrip.soa.caravan.configuration.source.memory.MemoryConfiguration;
import com.ctrip.soa.caravan.configuration.source.memory.MemoryConfigurationSource;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by w.jian on 2016/5/17.
 */

public class DefaultValueConfigurationSourceTest {
    final String planetKey = "planet";
    final String countryKey = "country";
    final String cityKey = "city";

    final String planetDefaultValue = "Earth";
    final String countryDefaultValue = "China";
    final String cityDefaultValue = "Shanghai";

    final String countryValue = "England";
    final String cityValue = "Landon";

    private DefaultValueConfigurationSource getDefaultValueConfigurationSource() {
        Map<String, String> map = new HashMap<>();
        map.put(planetKey, planetDefaultValue);
        map.put(countryKey, countryDefaultValue);
        map.put(cityKey, cityDefaultValue);
        return ConfigurationSources.newDefaultValueSource(map);
    }

    private MemoryConfigurationSource getMemoryConfigurationSource() {
        MemoryConfigurationSource memoryConfigurationSource = ConfigurationSources.newMemorySource(0, "test");
        MemoryConfiguration memoryConfiguration = memoryConfigurationSource.configuration();
        Map<String, String> map = new HashMap<>();
        map.put(countryKey, countryValue);
        map.put(cityKey, cityValue);
        memoryConfiguration.setProperties(map);
        return memoryConfigurationSource;
    }

    @Test
    public void defaultValueConfigurationSourceOnlyTest() {
        DefaultValueConfigurationSource source = getDefaultValueConfigurationSource();
        ConfigurationManager manager = ConfigurationManagers.newManager(source);

        String planet = manager.getPropertyValue(planetKey);
        String country = manager.getPropertyValue(countryKey);
        String city = manager.getPropertyValue(cityKey);

        Assert.assertEquals(planetDefaultValue, planet);
        Assert.assertEquals(countryDefaultValue, country);
        Assert.assertEquals(cityDefaultValue, city);
    }

    @Test
    public void defaultValueConfigurationSourceTest() {
        DefaultValueConfigurationSource defaultValueConfigurationSource = getDefaultValueConfigurationSource();
        MemoryConfigurationSource memoryConfigurationSource = getMemoryConfigurationSource();

        ConfigurationManager manager = ConfigurationManagers.newManager(defaultValueConfigurationSource, memoryConfigurationSource);
        String planet = manager.getPropertyValue(planetKey);
        String country = manager.getPropertyValue(countryKey);
        String city = manager.getPropertyValue(cityKey);

        Assert.assertEquals(planetDefaultValue, planet);
        Assert.assertEquals(countryValue, country);
        Assert.assertEquals(cityValue, city);
    }
}
