package com.ctrip.soa.caravan.web.configuration.source.servletcontext;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.ctrip.soa.caravan.web.configuration.source.servletcontext.ServletContextConfigurationSource;
import com.google.common.base.Strings;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class ServletContextConfigurationSourceTest {

    @Test
    public void testServletContextConfigurationSourceInt() {
        ServletContextConfigurationSource sccs = new ServletContextConfigurationSource(10, new MyServletContext());
        assertEquals(sccs.priority(), 10);
    }

    @Test
    public void testPriority() {
        ServletContextConfigurationSource sccs1 = new ServletContextConfigurationSource(1, new MyServletContext());
        assertEquals(sccs1.priority(), 1);
        ServletContextConfigurationSource sccs2 = new ServletContextConfigurationSource(0, new MyServletContext());
        assertEquals(sccs2.priority(), 0);
        ServletContextConfigurationSource sccs3 = new ServletContextConfigurationSource(-1, new MyServletContext());
        assertEquals(sccs3.priority(), -1);
    }

    @Test
    public void testConfiguration() {
        ServletContextConfigurationSource sccs = new ServletContextConfigurationSource(1, new MyServletContext());
        assertTrue(!Strings.isNullOrEmpty(sccs.configuration().getPropertyValue("env")));
    }
}