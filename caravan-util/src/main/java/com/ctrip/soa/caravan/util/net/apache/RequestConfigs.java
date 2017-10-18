package com.ctrip.soa.caravan.util.net.apache;

import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.Configurable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ctrip.soa.caravan.common.delegate.Func;
import com.ctrip.soa.caravan.common.value.ConcurrentHashMapValues;
import com.ctrip.soa.caravan.common.value.StringValues;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class RequestConfigs {

    public static final int DEFAULT_CONNECT_TIMEOUT = 10 * 1000;
    public static final int DEFAULT_CONNECTION_REQUEST_TIMEOUT = 1000;
    public static final int DEFAULT_SOCKET_TIMEOUT = 5 * 60 * 1000;

    public static final RequestConfig DEFAULT = RequestConfig.custom().setConnectionRequestTimeout(DEFAULT_CONNECTION_REQUEST_TIMEOUT)
            .setConnectTimeout(DEFAULT_CONNECT_TIMEOUT).setSocketTimeout(DEFAULT_SOCKET_TIMEOUT).build();

    private static final String DEFAULT_CONFIG_FIELD = "defaultConfig";
    private static final ConcurrentHashMap<Class<?>, AtomicReference<Field>> _clazzDefaultConfigFieldMap = new ConcurrentHashMap<>();

    private static final Logger _logger = LoggerFactory.getLogger(RequestConfigs.class);

    private RequestConfigs() {

    }

    public static RequestConfig createCascadedRequestConfig(Object client, RequestConfig requestConfig) {
        RequestConfig clientConfig = getRequestConfig(client);
        RequestConfig merged = createCascadedRequestConfig(clientConfig, requestConfig);
        return createCascadedRequestConfig(DEFAULT, merged);
    }

    public static RequestConfig createCascadedRequestConfig(RequestConfig genericConfig, RequestConfig concreteConfig) {
        if (genericConfig == null && concreteConfig == null)
            return null;

        if (genericConfig == null)
            return RequestConfig.copy(concreteConfig).build();

        if (concreteConfig == null)
            return RequestConfig.copy(genericConfig).build();

        RequestConfig.Builder builder = RequestConfig.copy(concreteConfig);

        if (concreteConfig.getConnectionRequestTimeout() <= 0)
            builder.setConnectionRequestTimeout(genericConfig.getConnectionRequestTimeout());

        if (concreteConfig.getConnectTimeout() <= 0)
            builder.setConnectTimeout(genericConfig.getConnectTimeout());

        if (concreteConfig.getSocketTimeout() <= 0)
            builder.setSocketTimeout(genericConfig.getSocketTimeout());

        if (StringValues.isNullOrWhitespace(concreteConfig.getCookieSpec()))
            builder.setCookieSpec(genericConfig.getCookieSpec());

        if (concreteConfig.getLocalAddress() == null)
            builder.setLocalAddress(genericConfig.getLocalAddress());

        if (concreteConfig.getProxy() == null)
            builder.setProxy(genericConfig.getProxy());

        if (concreteConfig.getMaxRedirects() <= 0)
            builder.setMaxRedirects(genericConfig.getMaxRedirects());

        if (concreteConfig.getTargetPreferredAuthSchemes() == null)
            builder.setTargetPreferredAuthSchemes(genericConfig.getTargetPreferredAuthSchemes());

        if (concreteConfig.getProxyPreferredAuthSchemes() == null)
            builder.setProxyPreferredAuthSchemes(genericConfig.getProxyPreferredAuthSchemes());

        return builder.build();
    }

    public static RequestConfig getRequestConfig(Object client) {
        if (client == null)
            return null;

        if (client instanceof Configurable)
            return ((Configurable) client).getConfig();

        return getRequestConfigFromField(client);
    }

    private static RequestConfig getRequestConfigFromField(Object client) {
        if (client == null)
            return null;

        final Class<?> clazz = client.getClass();
        AtomicReference<Field> defaultConfigFieldReference = ConcurrentHashMapValues.getOrAdd(_clazzDefaultConfigFieldMap, clazz,
                new Func<AtomicReference<Field>>() {
                    @Override
                    public AtomicReference<Field> execute() {
                        Field defaultConfigField = null;
                        try {
                            defaultConfigField = clazz.getDeclaredField(DEFAULT_CONFIG_FIELD);
                            if (defaultConfigField != null && defaultConfigField.getType() != RequestConfig.class) {
                                defaultConfigField = null;
                                _logger.info("Got a field defaultConfig from clazz {}, but not the type RequestConfig.", clazz);
                            } else {
                                defaultConfigField.setAccessible(true);
                                _logger.info("Got a field defaultConfig of type RequestConfig from clazz {}.", clazz);
                            }
                        } catch (Throwable ex) {
                            _logger.info("Cannot get a field defaultConfig from clazz " + clazz + ".", ex);
                        }

                        return new AtomicReference<Field>(defaultConfigField);
                    }
                });

        Field defaultConfigField = defaultConfigFieldReference.get();
        if (defaultConfigField == null)
            return null;

        try {
            return (RequestConfig) defaultConfigField.get(client);
        } catch (Throwable ex) {
            _logger.info("Cannot get value from defaultConfig field of clazz " + clazz, ex);
            return null;
        }
    }

}
