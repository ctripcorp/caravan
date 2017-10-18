package com.ctrip.soa.caravan.util.net.apache;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.StatusLine;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;

import com.ctrip.soa.caravan.common.serializer.StreamSerializer;
import com.ctrip.soa.caravan.util.serializer.JacksonJsonSerializer;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.io.CharStreams;
import com.google.common.net.HttpHeaders;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public final class HttpRequestExecutors {

    public static <T> T executeJson(String uri, String method, Class<T> clazz) {
        return executeJson(uri, method, null, null, clazz);
    }

    public static <T> T executeJson(String uri, Object data, Class<T> clazz) {
        return executeJson(uri, HttpPost.METHOD_NAME, null, null, data, false, clazz);
    }

    public static <T> T executeGzippedJson(String uri, Object data, Class<T> clazz) {
        return executeJson(uri, HttpPost.METHOD_NAME, null, null, data, true, clazz);
    }

    public static <T> T executeJson(CloseableHttpClient client, String uri, String method, Class<T> clazz) {
        return executeJson(client, uri, method, null, null, clazz);
    }

    public static <T> T executeJson(CloseableHttpClient client, String uri, Object data, Class<T> clazz) {
        return executeJson(client, uri, HttpPost.METHOD_NAME, null, null, data, false, clazz);
    }

    public static <T> T executeGzippedJson(CloseableHttpClient client, String uri, Object data, Class<T> clazz) {
        return executeJson(client, uri, HttpPost.METHOD_NAME, null, null, data, true, clazz);
    }

    public static <T> T executeJson(String uri, String method, ListMultimap<String, String> headers, RequestConfig config, Class<T> clazz) {
        return executeJson(uri, method, headers, config, null, false, clazz);
    }

    public static <T> T executeJson(String uri, String method, ListMultimap<String, String> headers, RequestConfig config, Object data, boolean gzipped,
            Class<T> clazz) {
        return execute(uri, method, headers, config, data, JacksonJsonSerializer.INSTANCE, gzipped, clazz);
    }

    public static <T> T executeJson(CloseableHttpClient client, String uri, String method, ListMultimap<String, String> headers, RequestConfig config,
            Class<T> clazz) {
        return executeJson(client, uri, method, headers, config, null, false, clazz);
    }

    public static <T> T executeJson(CloseableHttpClient client, String uri, String method, ListMultimap<String, String> headers, RequestConfig config,
            Object data, boolean gzipped, Class<T> clazz) {
        return execute(client, uri, method, headers, config, data, JacksonJsonSerializer.INSTANCE, gzipped, clazz);
    }

    public static String executeString(CloseableHttpClient client, String uri, ListMultimap<String, String> headers, RequestConfig config) {
        return executeString(client, uri, HttpGet.METHOD_NAME, headers, config, null, null, false);
    }

    public static <T> T execute(String uri, String method, ListMultimap<String, String> headers, RequestConfig config, StreamSerializer serializer,
            Class<T> clazz) {
        return execute(uri, method, headers, config, null, serializer, false, clazz);
    }

    public static <T> T execute(String uri, String method, ListMultimap<String, String> headers, RequestConfig config, Object data, StreamSerializer serializer,
            boolean gzipped, Class<T> clazz) {
        try (CloseableHttpClient client = createClient();) {
            return execute(client, uri, method, headers, config, data, serializer, gzipped, clazz);
        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    public static <T> T execute(CloseableHttpClient client, String uri, String method, ListMultimap<String, String> headers, RequestConfig config,
            StreamSerializer serializer, Class<T> clazz) {
        return execute(client, uri, method, headers, config, null, serializer, false, clazz);
    }

    public static <T> T execute(CloseableHttpClient client, String uri, String method, ListMultimap<String, String> headers, RequestConfig config, Object data,
            StreamSerializer serializer, boolean gzipped, Class<T> clazz) {
        config = RequestConfigs.createCascadedRequestConfig(client, config);

        if (serializer != null) {
            if (headers == null)
                headers = ArrayListMultimap.create();
            if (!headers.containsKey(HttpHeaders.CONTENT_TYPE))
                headers.put(HttpHeaders.CONTENT_TYPE, serializer.contentType());
            if (!headers.containsKey(HttpHeaders.ACCEPT))
                headers.put(HttpHeaders.ACCEPT, serializer.contentType());
        }

        HttpUriRequest request = HttpRequestFactory.createRequest(uri, method, headers, config, data, serializer, gzipped);
        return execute(client, request, serializer, clazz);
    }

    public static String executeString(CloseableHttpClient client, String uri, String method, ListMultimap<String, String> headers, RequestConfig config,
            Object data, StreamSerializer serializer, boolean gzipped) {
        config = RequestConfigs.createCascadedRequestConfig(client, config);

        if (serializer != null) {
            if (headers == null)
                headers = ArrayListMultimap.create();
            if (!headers.containsKey(HttpHeaders.CONTENT_TYPE))
                headers.put(HttpHeaders.CONTENT_TYPE, serializer.contentType());
            if (!headers.containsKey(HttpHeaders.ACCEPT))
                headers.put(HttpHeaders.ACCEPT, serializer.contentType());
        }

        HttpUriRequest request = HttpRequestFactory.createRequest(uri, method, headers, config, data, serializer, gzipped);
        return executeString(client, request);
    }

    public static <T> T execute(CloseableHttpClient client, HttpUriRequest request, StreamSerializer serializer, Class<T> clazz) {
        try (CloseableHttpResponse response = client.execute(request);) {
            StatusLine statusLine = response.getStatusLine();
            if (statusLine == null)
                throw new RuntimeException("Request failed. StatusLine is null.");

            if (statusLine.getStatusCode() >= 400)
                throw new HttpRequestException(response);

            try (InputStream is = response.getEntity().getContent();) {
                return serializer.deserialize(is, clazz);
            }
        } catch (IllegalStateException | IOException ex) {
            throw new HttpConnectException(ex.getMessage(), ex);
        }
    }

    public static String executeString(CloseableHttpClient client, HttpUriRequest request) {
        try (CloseableHttpResponse response = client.execute(request);) {
            StatusLine statusLine = response.getStatusLine();
            if (statusLine == null)
                throw new RuntimeException("Request failed. StatusLine is null.");

            if (statusLine.getStatusCode() >= 400)
                throw new HttpRequestException(response);

            try (InputStream is = response.getEntity().getContent();) {
                return CharStreams.toString(new InputStreamReader(is));
            }
        } catch (IllegalStateException | IOException ex) {
            throw new HttpConnectException(ex.getMessage(), ex);
        }
    }

    private static CloseableHttpClient createClient() {
        return PoolingHttpClientFactory.create(RequestConfigs.DEFAULT, IOExceptionRetryHelper.DEFAULT.retryHandler(), null);
    }

    private HttpRequestExecutors() {

    }

}
