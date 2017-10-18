package com.ctrip.soa.caravan.web.configuration.source.servletcontext;

import javax.servlet.ServletContext;

import com.ctrip.soa.caravan.configuration.AbstractConfigurationSource;
import com.ctrip.soa.caravan.configuration.Configuration;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class ServletContextConfigurationSource extends AbstractConfigurationSource {

    private ServletContextConfiguration _configuration;

    public ServletContextConfigurationSource(final int priority, final ServletContext context) {
        super(priority, "ServletContext");
        _configuration = new ServletContextConfiguration(context);
    }

    @Override
    public Configuration configuration() {
        return _configuration;
    }
}