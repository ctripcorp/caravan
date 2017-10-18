package com.ctrip.soa.caravan.configuration.source.memory;

import org.junit.Assert;
import org.junit.Test;

import com.ctrip.soa.caravan.common.concurrent.Threads;
import com.ctrip.soa.caravan.configuration.dynamic.DynamicConfigurationManager;
import com.ctrip.soa.caravan.configuration.dynamic.PropertyChangeEvent;
import com.ctrip.soa.caravan.configuration.dynamic.PropertyChangeListener;
import com.ctrip.soa.caravan.configuration.facade.ConfigurationManagers;
import com.ctrip.soa.caravan.configuration.facade.ConfigurationSources;
import com.ctrip.soa.caravan.configuration.facade.TypedDynamicCachedCorrectedProperties;
import com.ctrip.soa.caravan.configuration.typed.dynamic.TypedDynamicProperty;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by w.jian on 2016/5/18.
 */

public class MemoryConfigurationSourceTest {
    final String planetKey = "planet";
    final String countryKey = "country";
    final String cityKey = "city";

    final String planetDefaultValue = "Earth";
    final String countryDefaultValue = "China";
    final String cityDefaultValue = "Shanghai";

    final String countryValue = "England";
    final String cityValue = "Landon";

    private MemoryConfigurationSource getMemoryConfigurationSource() {
        MemoryConfigurationSource memoryConfigurationSource = ConfigurationSources.newMemorySource(0, "test");
        setDefaultProperties(memoryConfigurationSource);
        return memoryConfigurationSource;
    }

    private void setDefaultProperties(MemoryConfigurationSource source) {
        MemoryConfiguration memoryConfiguration = source.configuration();
        Map<String, String> map = new HashMap<>();
        map.put(planetKey, planetDefaultValue);
        map.put(countryKey, countryDefaultValue);
        map.put(cityKey, cityDefaultValue);
        memoryConfiguration.setProperties(map);
    }

    private void setProperties(MemoryConfigurationSource source) {
        MemoryConfiguration memoryConfiguration = source.configuration();
        Map<String, String> map = new HashMap<>();
        map.put(countryKey, countryValue);
        map.put(cityKey, cityValue);
        memoryConfiguration.setProperties(map);
    }

    @Test
    public void test() {
        MemoryConfigurationSource source = getMemoryConfigurationSource();
        DynamicConfigurationManager manager = ConfigurationManagers.newDynamicManager(source);

        String planet = manager.getPropertyValue(planetKey);
        String country = manager.getPropertyValue(countryKey);
        String city = manager.getPropertyValue(cityKey);

        Assert.assertEquals(planetDefaultValue, planet);
        Assert.assertEquals(countryDefaultValue, country);
        Assert.assertEquals(cityDefaultValue, city);

        final Boolean[] listenerExecuted = { false, false };
        manager.addPropertyChangeListener(countryKey, new PropertyChangeListener() {
            @Override
            public void onChange(PropertyChangeEvent event) {
                Assert.assertEquals(countryDefaultValue, event.oldValue());
                Assert.assertEquals(countryValue, event.newValue());
                listenerExecuted[0] = true;
            }
        });

        manager.addPropertyChangeListener(cityKey, new PropertyChangeListener() {
            @Override
            public void onChange(PropertyChangeEvent event) {
                Assert.assertEquals(cityDefaultValue, event.oldValue());
                Assert.assertEquals(cityValue, event.newValue());
                listenerExecuted[1] = true;
            }
        });

        setProperties(source);

        Threads.sleep(20);
        Assert.assertTrue(listenerExecuted[0]);
        Assert.assertTrue(listenerExecuted[1]);
    }

    @Test
    public void test2() {
        MemoryConfigurationSource source = getMemoryConfigurationSource();
        TypedDynamicCachedCorrectedProperties properties = new TypedDynamicCachedCorrectedProperties(source);
        TypedDynamicProperty<String> planetProperty = properties.getStringProperty(planetKey);
        TypedDynamicProperty<String> countryProperty = properties.getStringProperty(countryKey);
        TypedDynamicProperty<String> cityProperty = properties.getStringProperty(cityKey);

        Assert.assertEquals(planetDefaultValue, planetProperty.typedValue());
        Assert.assertEquals(countryDefaultValue, countryProperty.typedValue());
        Assert.assertEquals(cityDefaultValue, cityProperty.typedValue());

        final Boolean[] listenerExecuted = { false, false };
        countryProperty.addChangeListener(new PropertyChangeListener() {
            @Override
            public void onChange(PropertyChangeEvent event) {
                Assert.assertEquals(countryDefaultValue, event.oldValue());
                Assert.assertEquals(countryValue, event.newValue());
                listenerExecuted[0] = true;
            }
        });

        cityProperty.addChangeListener(new PropertyChangeListener() {
            @Override
            public void onChange(PropertyChangeEvent event) {
                Assert.assertEquals(cityDefaultValue, event.oldValue());
                Assert.assertEquals(cityValue, event.newValue());
                listenerExecuted[1] = true;
            }
        });

        setProperties(source);

        Threads.sleep(20);

        Assert.assertTrue(listenerExecuted[0]);
        Assert.assertTrue(listenerExecuted[1]);

        Assert.assertEquals(countryValue, countryProperty.typedValue());
        Assert.assertEquals(cityValue, cityProperty.typedValue());
    }

}
