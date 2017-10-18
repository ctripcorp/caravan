package com.ctrip.soa.caravan.web.configuration.source.servletcontext;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ctrip.soa.caravan.common.value.StringValues;
import com.ctrip.soa.caravan.common.value.checker.NullArgumentChecker;
import com.ctrip.soa.caravan.configuration.Configuration;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class ServletContextConfiguration implements Configuration {

    private static final Logger _logger = LoggerFactory.getLogger(ServletContextConfiguration.class);

    private ServletContext _context;

    public ServletContextConfiguration(final ServletContext context) {
        NullArgumentChecker.DEFAULT.check(context, "context");
        _context = context;
    }

    @Override
    public String getPropertyValue(String key) {
        if (StringValues.isNullOrWhitespace(key)) {
            _logger.warn("ServletContext key is null or empty!");
            return null;
        }
        String value = _context.getInitParameter(key);
        return value;
    }
}