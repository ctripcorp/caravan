package com.ctrip.soa.caravan.configuration.sources.cascaded;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.ctrip.soa.caravan.common.concurrent.Threads;
import com.ctrip.soa.caravan.common.net.NetworkInterfaceManager;
import com.ctrip.soa.caravan.configuration.facade.ConfigurationManagers;
import com.ctrip.soa.caravan.configuration.facade.ConfigurationSources;
import com.ctrip.soa.caravan.configuration.facade.TypedDynamicCachedCorrectedProperties;
import com.ctrip.soa.caravan.configuration.source.cascaded.CascadedConfigurationSource;
import com.ctrip.soa.caravan.configuration.source.memory.MemoryConfigurationSource;
import com.ctrip.soa.caravan.configuration.typed.dynamic.TypedDynamicProperty;
import com.ctrip.soa.caravan.configuration.typed.dynamic.cached.corrected.TypedDynamicCachedCorrectedConfigurationManager;
import com.ctrip.soa.caravan.configuration.typed.dynamic.cached.corrected.TypedDynamicCachedCorrectedProperty;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class CascadedConfigurationSourceTest {

    @Test
    public void CascadedKeyTest() {
        String ip = NetworkInterfaceManager.INSTANCE.localhostIP();
        String key = "soa.ok";
        String key2 = key + "." + ip;
        String value = "1";
        String value2 = "2";

        MemoryConfigurationSource source = ConfigurationSources.newMemorySource(1, "memory-01");
        source.configuration().setPropertyValue(key, value);
        source.configuration().setPropertyValue(key2, value2);

        CascadedConfigurationSource source2 = ConfigurationSources.newCascadedSource(source, new String[] { ip });
        String result = source2.configuration().getPropertyValue(key);
        assertEquals(value2, result);

        String result2 = source2.configuration().getPropertyValue(key2);
        assertEquals(value2, result2);
    }

    @Test
    public void CascadedKeyInTypedDynamicTest() {
        String ip = NetworkInterfaceManager.INSTANCE.localhostIP();
        String key = "soa.ok";
        String key2 = key + "." + ip;
        String value = "1";
        String value2 = "2";

        MemoryConfigurationSource source = ConfigurationSources.newMemorySource(1, "memory-01");
        source.configuration().setPropertyValue(key, value);
        source.configuration().setPropertyValue(key2, value2);
        CascadedConfigurationSource source2 = ConfigurationSources.newCascadedSource(source, new String[] { ip });
        TypedDynamicCachedCorrectedConfigurationManager manager = ConfigurationManagers.newTypedDynamicCachedCorrectedManager(source2);
        TypedDynamicCachedCorrectedProperties properties = new TypedDynamicCachedCorrectedProperties(manager);

        TypedDynamicProperty<Integer> property = properties.getIntProperty(key, 0);
        assertTrue(2 == property.typedValue());
        TypedDynamicProperty<Integer> property2 = properties.getIntProperty(key2);
        assertTrue(2 == property2.typedValue());
    }

    @Test
    public void CascadedKeyInTypedDynamicTest2() throws InterruptedException {
        String ip = NetworkInterfaceManager.INSTANCE.localhostIP();
        String originalKey = "soa.test";
        String cascadedKey = originalKey + "." + ip;
        String originalValue = "1";
        String cascadedValue = "2";

        MemoryConfigurationSource mcs = ConfigurationSources.newMemorySource(1, "memory-01");
        mcs.configuration().setPropertyValue(originalKey, originalValue);
        mcs.configuration().setPropertyValue(cascadedKey, cascadedValue);

        CascadedConfigurationSource ccs = ConfigurationSources.newCascadedDynamicSource(mcs, new String[] { ip });

        TypedDynamicCachedCorrectedConfigurationManager manager = ConfigurationManagers.newTypedDynamicCachedCorrectedManager(ccs);
        TypedDynamicCachedCorrectedProperties properties = new TypedDynamicCachedCorrectedProperties(manager);
        TypedDynamicCachedCorrectedProperty<Integer> originalProperty = properties.getIntProperty(originalKey, 0);
        TypedDynamicCachedCorrectedProperty<Integer> cascadedProperty = properties.getIntProperty(cascadedKey, 0);

        assertTrue(2 == originalProperty.typedValue());
        assertTrue(2 == cascadedProperty.typedValue());

        mcs.configuration().setPropertyValue(cascadedKey, "11");
        Threads.sleep(20);
        assertEquals(originalProperty.typedValue(), Integer.valueOf(11));
        assertEquals(cascadedProperty.typedValue(), Integer.valueOf(11));

        mcs.configuration().setPropertyValue(cascadedKey, null);
        Threads.sleep(20);
        assertEquals(originalProperty.typedValue(), Integer.valueOf(1));
        assertEquals(cascadedProperty.typedValue(), Integer.valueOf(0));
    }
}