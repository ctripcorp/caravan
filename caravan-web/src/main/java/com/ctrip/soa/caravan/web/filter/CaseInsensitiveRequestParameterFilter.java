package com.ctrip.soa.caravan.web.filter;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import com.ctrip.soa.caravan.common.value.MapValues;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class CaseInsensitiveRequestParameterFilter implements Filter {

    private static class CaseInsensitiveRequestParameterHttpServletWrapper extends HttpServletRequestWrapper {

        public CaseInsensitiveRequestParameterHttpServletWrapper(HttpServletRequest request) {
            super(request);
        }

        @Override
        public String[] getParameterValues(String name) {
            if (name == null)
                return null;

            Map<String, String[]> parameterMap = super.getParameterMap();
            if (MapValues.isNullOrEmpty(parameterMap))
                return null;

            for (String key : parameterMap.keySet()) {
                if (name.equalsIgnoreCase(key))
                    return parameterMap.get(key);
            }

            return null;
        }

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        chain.doFilter(new CaseInsensitiveRequestParameterHttpServletWrapper((HttpServletRequest) request), response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }
}
