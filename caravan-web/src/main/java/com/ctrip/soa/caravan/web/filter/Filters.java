package com.ctrip.soa.caravan.web.filter;

import java.util.EnumSet;
import java.util.Map;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ctrip.soa.caravan.common.value.checker.CollectionArgumentChecker;
import com.ctrip.soa.caravan.common.value.checker.NullArgumentChecker;
import com.ctrip.soa.caravan.common.value.checker.StringArgumentChecker;
import com.github.ziplet.filter.compression.CompressingFilter;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public final class Filters {

    private static final String DEFAULT_COMPRESSING_FILTER_NAME = "compressing-filter";
    private static final String DEFAULT_CROSS_DOMAIN_FILTER_NAME = "cross-domain-filter";
    private static final String DEFAULT_CASE_INSENSITIVE_REQUEST_PARAMETER_FILTER_NAME = "case-insensitive-request-parameter-filter";

    private static final String ROOT_URL_PATTERN = "/*";

    private static final Logger _logger = LoggerFactory.getLogger(Filters.class);

    private Filters() {

    }

    public static Filter newCompressingFilter() {
        return new CompressingFilter();
    }

    public static Filter newCompressingFilter(ServletContext context, String filterName, Map<String, String> initParams) {
        CompressingFilter compressingFilter = new CompressingFilter();

        FilterConfig filterConfig = new CustomFilterConfig(context, filterName, initParams);
        try {
            compressingFilter.init(filterConfig);
        } catch (Throwable ex) {
            _logger.error("CompressingFilter init failed.", ex);
        }

        return compressingFilter;
    }

    public static void registerCompressingFilter(ServletContext context) {
        registerCompressingFilter(context, ROOT_URL_PATTERN);
    }

    public static void registerCompressingFilter(ServletContext context, String... urlPatterns) {
        registerFilter(context, DEFAULT_COMPRESSING_FILTER_NAME, newCompressingFilter(), urlPatterns);
    }

    public static Filter newCrossDomainFilter() {
        return new CrossDomainFilter();
    }

    public static void registerCrossDomainFilter(ServletContext context) {
        registerCrossDomainFilter(context, ROOT_URL_PATTERN);
    }

    public static void registerCrossDomainFilter(ServletContext context, String... urlPatterns) {
        registerFilter(context, DEFAULT_CROSS_DOMAIN_FILTER_NAME, newCrossDomainFilter(), urlPatterns);
    }

    public static Filter newCaseInsensitiveRequestParameterFilter() {
        return new CaseInsensitiveRequestParameterFilter();
    }

    public static void registerCaseInsensitiveRequestParameterFilter(ServletContext context) {
        registerCaseInsensitiveRequestParameterFilter(context, ROOT_URL_PATTERN);
    }

    public static void registerCaseInsensitiveRequestParameterFilter(ServletContext context, String... urlPatterns) {
        registerFilter(context, DEFAULT_CASE_INSENSITIVE_REQUEST_PARAMETER_FILTER_NAME, newCaseInsensitiveRequestParameterFilter(), urlPatterns);
    }

    public static void registerFilter(ServletContext context, String filterName, Filter filter, String... urlPatterns) {
        registerFilter(context, filterName, filter, EnumSet.allOf(DispatcherType.class), false, urlPatterns);
    }

    public static FilterRegistration.Dynamic registerFilter(ServletContext context, String filterName, Filter filter, EnumSet<DispatcherType> dispatcherTypes,
            boolean isMatchingAfter, String... urlPatterns) {
        NullArgumentChecker.DEFAULT.check(context, "context");
        StringArgumentChecker.DEFAULT.check(filterName, "filterName");
        NullArgumentChecker.DEFAULT.check(filter, "filter");
        CollectionArgumentChecker.DEFAULT.check(dispatcherTypes, "dispatcherTypes");
        NullArgumentChecker.DEFAULT.check(urlPatterns, "urlMapping");

        FilterRegistration.Dynamic filterRegistration = context.addFilter(filterName, filter);
        filterRegistration.addMappingForUrlPatterns(dispatcherTypes, isMatchingAfter, urlPatterns);

        return filterRegistration;
    }

}
