package com.ctrip.soa.caravan.configuration.defaultvalue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.ctrip.soa.caravan.configuration.Property;
import com.ctrip.soa.caravan.configuration.facade.ConfigurationManagers;
import com.ctrip.soa.caravan.configuration.facade.ConfigurationSources;
import com.ctrip.soa.caravan.configuration.source.defaultvalue.DefaultValueConfigurationSource;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class DefaultValuePropertyTest {
    final String planetKey = "planet";
    final String countryKey = "country";
    final String cityKey = "city";

    final String planetDefaultValue = "Earth";
    final String countryDefaultValue = "China";
    final String cityDefaultValue = "Shanghai";

    @Test
    public void defaultNullConfigurationSourceTest() {
        Map<String, String> map = new HashMap<>();
        DefaultValueConfigurationSource defaultValueConfigurationSource = ConfigurationSources
                .newDefaultValueSource(map);
        DefaultValueConfigurationManager manager = ConfigurationManagers
                .newDefaultValueManager(defaultValueConfigurationSource);
        String planet = manager.getPropertyValue(planetKey, planetDefaultValue);
        String country = manager.getPropertyValue(countryKey, countryDefaultValue);
        String city = manager.getPropertyValue(cityKey, cityDefaultValue);

        Assert.assertEquals(planetDefaultValue, planet);
        Assert.assertEquals(countryDefaultValue, country);
        Assert.assertEquals(cityDefaultValue, city);

        Property p1 = manager.getProperty(planetKey, planetDefaultValue);
        Assert.assertEquals(planetDefaultValue, p1.value());
    }

    @Test
    public void defaultValueConfigurationSourceTest() {
        Map<String, String> map = new HashMap<>();
        map.put(planetKey, "123");
        map.put(countryKey, "456");
        map.put(cityKey, "789");
        DefaultValueConfigurationSource defaultValueConfigurationSource = ConfigurationSources
                .newDefaultValueSource(map);
        DefaultValueConfigurationManager manager = ConfigurationManagers
                .newDefaultValueManager(defaultValueConfigurationSource);
        String planet = manager.getPropertyValue(planetKey, planetDefaultValue);
        String country = manager.getPropertyValue(countryKey, countryDefaultValue);
        String city = manager.getPropertyValue(cityKey, cityDefaultValue);

        Assert.assertEquals("123", planet);
        Assert.assertEquals("456", country);
        Assert.assertEquals("789", city);

        Property p1 = manager.getProperty(planetKey, planetDefaultValue);
        Assert.assertEquals("123", p1.value());
    }

    @Test
    public void defaultNullToValueConfigurationSourceTest() {
        Map<String, String> map = new HashMap<>();
        DefaultValueConfigurationSource defaultValueConfigurationSource = ConfigurationSources
                .newDefaultValueSource(map);
        DefaultValueConfigurationManager manager = ConfigurationManagers
                .newDefaultValueManager(defaultValueConfigurationSource);
        String planet = manager.getPropertyValue(planetKey, planetDefaultValue);
        String country = manager.getPropertyValue(countryKey, countryDefaultValue);
        String city = manager.getPropertyValue(cityKey, cityDefaultValue);

        Assert.assertEquals(planetDefaultValue, planet);
        Assert.assertEquals(countryDefaultValue, country);
        Assert.assertEquals(cityDefaultValue, city);

        Property p1 = manager.getProperty(planetKey, planetDefaultValue);
        Assert.assertEquals(planetDefaultValue, p1.value());

        map.put(planetKey, "123");
        map.put(countryKey, "456");
        map.put(cityKey, "789");
        defaultValueConfigurationSource = ConfigurationSources.newDefaultValueSource(map);
        manager = ConfigurationManagers.newDefaultValueManager(defaultValueConfigurationSource);
        String planet1 = manager.getPropertyValue(planetKey, planetDefaultValue);
        String country1 = manager.getPropertyValue(countryKey, countryDefaultValue);
        String city1 = manager.getPropertyValue(cityKey, cityDefaultValue);

        Assert.assertEquals("123", planet1);
        Assert.assertEquals("456", country1);
        Assert.assertEquals("789", city1);

        Property p2 = manager.getProperty(planetKey, planetDefaultValue);
        Assert.assertEquals("123", p2.value());
    }

    @Test
    public void defaultValueToNullConfigurationSourceTest() {
        Map<String, String> map = new HashMap<>();
        map.put(planetKey, "123");
        map.put(countryKey, "456");
        map.put(cityKey, "789");
        DefaultValueConfigurationSource defaultValueConfigurationSource = ConfigurationSources
                .newDefaultValueSource(map);
        DefaultValueConfigurationManager manager = ConfigurationManagers
                .newDefaultValueManager(defaultValueConfigurationSource);
        String planet1 = manager.getPropertyValue(planetKey, planetDefaultValue);
        String country1 = manager.getPropertyValue(countryKey, countryDefaultValue);
        String city1 = manager.getPropertyValue(cityKey, cityDefaultValue);

        Assert.assertEquals("123", planet1);
        Assert.assertEquals("456", country1);
        Assert.assertEquals("789", city1);

        Property p2 = manager.getProperty(planetKey, planetDefaultValue);
        Assert.assertEquals("123", p2.value());

        map = new HashMap<>();
        defaultValueConfigurationSource = ConfigurationSources.newDefaultValueSource(map);
        manager = ConfigurationManagers.newDefaultValueManager(defaultValueConfigurationSource);
        String planet = manager.getPropertyValue(planetKey, planetDefaultValue);
        String country = manager.getPropertyValue(countryKey, countryDefaultValue);
        String city = manager.getPropertyValue(cityKey, cityDefaultValue);

        Assert.assertEquals(planetDefaultValue, planet);
        Assert.assertEquals(countryDefaultValue, country);
        Assert.assertEquals(cityDefaultValue, city);

        Property p1 = manager.getProperty(planetKey, planetDefaultValue);
        Assert.assertEquals(planetDefaultValue, p1.value());

    }
}
