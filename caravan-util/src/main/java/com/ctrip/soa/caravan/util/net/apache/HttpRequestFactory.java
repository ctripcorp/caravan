package com.ctrip.soa.caravan.util.net.apache;

import java.io.ByteArrayOutputStream;
import java.util.Map;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.GzipCompressingEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntity;

import com.ctrip.soa.caravan.common.serializer.StreamSerializer;
import com.ctrip.soa.caravan.common.value.checker.NullArgumentChecker;
import com.ctrip.soa.caravan.common.value.checker.StringArgumentChecker;
import com.ctrip.soa.caravan.util.serializer.JacksonJsonSerializer;
import com.google.common.collect.ListMultimap;
import com.google.common.net.HttpHeaders;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public final class HttpRequestFactory {

    public static HttpUriRequest createJsonRequest(String uri, String method, RequestConfig config, Object data) {
        return createJsonRequest(uri, method, null, config, data, false);
    }

    public static HttpUriRequest createJsonRequest(String uri, String method, ListMultimap<String, String> headers, RequestConfig config, Object data,
            boolean gzipped) {
        return createRequest(uri, method, headers, config, data, JacksonJsonSerializer.INSTANCE, gzipped);
    }

    public static HttpUriRequest createGzippedRequest(String uri, String method, ListMultimap<String, String> headers, RequestConfig config, Object data,
            StreamSerializer serializer) {
        return createRequest(uri, method, headers, config, data, serializer, true);
    }

    public static HttpUriRequest createRequest(String uri, String method, ListMultimap<String, String> headers, RequestConfig config, Object data,
            StreamSerializer serializer, boolean gzipped) {
        StringArgumentChecker.DEFAULT.check(uri, "uri");
        StringArgumentChecker.DEFAULT.check(method, "method");

        uri = uri.trim();
        method = method.trim().toUpperCase();

        HttpRequestBase request = null;
        switch (method) {
            case HttpPost.METHOD_NAME:
            case HttpPut.METHOD_NAME:
                NullArgumentChecker.DEFAULT.check(data, "data");
                NullArgumentChecker.DEFAULT.check(serializer, "serializer");

                HttpEntityEnclosingRequestBase entityRequest = createEntityEnclosingRequest(uri, method);
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                serializer.serialize(os, data);
                ByteArrayEntity entity = new ByteArrayEntity(os.toByteArray());
                if (gzipped) {
                    GzipCompressingEntity gzippedEntity = new GzipCompressingEntity(entity);
                    entityRequest.setEntity(gzippedEntity);
                    entityRequest.addHeader(gzippedEntity.getContentEncoding());
                } else {
                    entityRequest.setEntity(entity);
                }

                request = entityRequest;
                if (!request.containsHeader(HttpHeaders.CONTENT_TYPE))
                    request.addHeader(HttpHeaders.CONTENT_TYPE, serializer.contentType());
                if (!request.containsHeader(HttpHeaders.ACCEPT))
                    request.addHeader(HttpHeaders.ACCEPT, serializer.contentType());
                break;
            case HttpGet.METHOD_NAME:
                request = new HttpGet(uri);
                break;
            case HttpDelete.METHOD_NAME:
                request = new HttpDelete(uri);
                break;
            case HttpHead.METHOD_NAME:
                request = new HttpHead(uri);
                break;
            case HttpOptions.METHOD_NAME:
                request = new HttpOptions(uri);
                break;
            default:
                throw new IllegalArgumentException("The method is not supported: " + method);
        }

        if (headers != null) {
            for (Map.Entry<String, String> header : headers.entries()) {
                request.addHeader(header.getKey(), header.getValue());
            }
        }

        if (config != null)
            request.setConfig(config);

        return request;
    }

    private static HttpEntityEnclosingRequestBase createEntityEnclosingRequest(String uri, String method) {
        switch (method) {
            case HttpPut.METHOD_NAME:
                return new HttpPut(uri);
            default:
                return new HttpPost(uri);
        }
    }

    private HttpRequestFactory() {

    }

}
