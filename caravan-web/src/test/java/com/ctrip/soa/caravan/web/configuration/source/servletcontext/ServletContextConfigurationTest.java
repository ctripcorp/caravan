package com.ctrip.soa.caravan.web.configuration.source.servletcontext;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.ctrip.soa.caravan.web.configuration.source.servletcontext.ServletContextConfiguration;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class ServletContextConfigurationTest {

    @Test
    public void testGetPropertyValue() {
        ServletContextConfiguration scc = new ServletContextConfiguration(new MyServletContext());
        assertEquals(scc.getPropertyValue("service-port"), "8090");
        assertEquals(scc.getPropertyValue("env"), "fws");
    }
}