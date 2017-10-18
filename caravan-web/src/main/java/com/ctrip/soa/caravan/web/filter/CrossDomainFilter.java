package com.ctrip.soa.caravan.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ctrip.soa.caravan.common.value.StringValues;
import com.google.common.net.HttpHeaders;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class CrossDomainFilter implements Filter {

    private String allowOrigin = "*";
    private String allowMethods = "*";
    private String allowHeaders = "*";
    private String exposeHeaders = "*";
    private String allowCredentials = "true";
    private String maxAge = String.valueOf(60 * 60);

    public String getAllowOrigin() {
        return allowOrigin;
    }

    public void setAllowOrigin(String allowOrigin) {
        if (StringValues.isNullOrWhitespace(allowOrigin))
            return;

        this.allowOrigin = allowOrigin;
    }

    public String getAllowMethods() {
        return allowMethods;
    }

    public void setAllowMethods(String allowMethods) {
        if (StringValues.isNullOrWhitespace(allowMethods))
            return;

        this.allowMethods = allowMethods;
    }

    public String getAllowHeaders() {
        return allowHeaders;
    }

    public void setAllowHeaders(String allowHeaders) {
        if (StringValues.isNullOrWhitespace(allowHeaders))
            return;

        this.allowHeaders = allowHeaders;
    }

    public String getExposeHeaders() {
        return exposeHeaders;
    }

    public void setExposeHeaders(String exposeHeaders) {
        if (StringValues.isNullOrWhitespace(exposeHeaders))
            return;

        this.exposeHeaders = exposeHeaders;
    }

    public boolean isAllowCredentials() {
        return "true".equals(allowCredentials);
    }

    public void setAllowCredentials(boolean allowCredentials) {
        this.allowCredentials = allowCredentials ? "true" : "false";
    }

    public long getMaxAge() {
        return Long.parseLong(maxAge);
    }

    public void setMaxAge(long maxAge) {
        this.maxAge = String.valueOf(maxAge);
    }

    public CrossDomainFilter() {

    }

    public CrossDomainFilter(String allowOrigin, String allowMethods, String allowHeaders, String exposeHeaders, boolean allowCredentials, long maxAge) {
        setAllowOrigin(allowOrigin);
        setAllowMethods(allowMethods);
        setAllowHeaders(allowHeaders);
        setExposeHeaders(exposeHeaders);
        setAllowCredentials(allowCredentials);
        setMaxAge(maxAge);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    @SuppressWarnings("all")
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
            chain.doFilter(request, response);
            return;
        }

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        res.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, allowOrigin);
        res.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, exposeHeaders);

        if (isAllowCredentials())
            res.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, allowCredentials);

        if (!"OPTIONS".equals(req.getMethod())) {
            chain.doFilter(request, response);
            return;
        }

        String accessControllRequestMethod = req.getHeader(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD);
        if (accessControllRequestMethod != null)
            res.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, accessControllRequestMethod);

        String accessControllRequestHeaders = req.getHeader(HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS);
        if (accessControllRequestHeaders != null)
            res.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, accessControllRequestHeaders);

        res.setHeader(HttpHeaders.ACCESS_CONTROL_MAX_AGE, maxAge);

        res.setStatus(200);
    }

    @Override
    public void destroy() {

    }

}
