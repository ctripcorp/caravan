package com.ctrip.soa.caravan.configuration.cached;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.ctrip.soa.caravan.configuration.MyConfigurationSource;
import com.ctrip.soa.caravan.configuration.facade.ConfigurationManagers;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class DefaultCachedConfigurationManagerTest {

    @Test
    public void testGetPropertyString() {
        MyConfigurationSource mcs1 = new MyConfigurationSource(1);
        MyConfigurationSource mcs2 = new MyConfigurationSource(2);
        MyConfigurationSource[] mcsArray = {mcs1, mcs2};
        DefaultCachedConfigurationManager m = (DefaultCachedConfigurationManager) ConfigurationManagers.newCachedManager(mcsArray);
        assertEquals(m.getPropertyValue("k1"), "my: 2");
    }
}