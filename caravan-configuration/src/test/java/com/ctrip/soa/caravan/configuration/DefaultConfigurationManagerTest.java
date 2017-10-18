package com.ctrip.soa.caravan.configuration;

import org.junit.Test;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class DefaultConfigurationManagerTest {

    @Test
    public void testInit() {
        MyConfigurationSource mcs1 = new MyConfigurationSource(1);
        MyConfigurationSource mcs2 = new MyConfigurationSource(2);
        MyConfigurationSource mcs3 = new MyConfigurationSource(3);
        MyConfigurationSource mcs4 = new MyConfigurationSource(4);
        MyConfigurationSource mcs5 = new MyConfigurationSource(5);
        MyConfigurationSource[] testCSArray = {mcs4, mcs3, mcs5, mcs1, mcs2};
        DefaultConfigurationManager manager = new DefaultConfigurationManager(testCSArray);
        for (ConfigurationSource cs : manager.sources()) {
            System.out.println("The source value: " + cs.configuration().getPropertyValue("key"));
        }
        System.out.println("The default first priority source value: " + manager.getPropertyValue("key"));
    }
}