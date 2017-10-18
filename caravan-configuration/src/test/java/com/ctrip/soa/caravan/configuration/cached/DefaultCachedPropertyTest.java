package com.ctrip.soa.caravan.configuration.cached;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.ctrip.soa.caravan.configuration.MyProperty;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class DefaultCachedPropertyTest {

    @Test
    public void testValue() {
        MyProperty mp = new MyProperty("k1", "v1");
        DefaultCachedProperty dcp = new DefaultCachedProperty(mp);
        assertEquals(dcp.value(), "v1");
    }

    @Test
    public void testRefresh() {
        MyProperty mp = new MyProperty("k1", "v1");
        DefaultCachedProperty dcp = new DefaultCachedProperty(mp);
        dcp.refresh();
    }
}