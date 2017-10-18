package com.ctrip.soa.caravan.web.filter;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;

import com.ctrip.soa.caravan.common.value.checker.NullArgumentChecker;
import com.ctrip.soa.caravan.common.value.checker.StringArgumentChecker;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class CustomFilterConfig implements FilterConfig {

    private ServletContext _context;
    private String _filterName;
    private Map<String, String> _initParams;

    public CustomFilterConfig(ServletContext context, String filterName, Map<String, String> initParams) {
        NullArgumentChecker.DEFAULT.check(context, "context");
        StringArgumentChecker.DEFAULT.check(filterName, "filterName");
        NullArgumentChecker.DEFAULT.check(initParams, "initParams");

        _context = context;
        _filterName = filterName;
        _initParams = initParams;
    }

    @Override
    public String getFilterName() {
        return _filterName;
    }

    @Override
    public ServletContext getServletContext() {
        return _context;
    }

    @Override
    public String getInitParameter(String name) {
        return _initParams.get(name);
    }

    @Override
    public Enumeration<String> getInitParameterNames() {
        return Collections.enumeration(_initParams.keySet());
    }

}
