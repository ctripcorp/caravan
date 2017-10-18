package com.ctrip.soa.caravan.util.net.apache.async;

import java.util.concurrent.Executor;

import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import com.ctrip.soa.caravan.common.serializer.StreamSerializer;
import com.ctrip.soa.caravan.common.value.checker.NullArgumentChecker;
import com.ctrip.soa.caravan.util.net.apache.HttpRequestFactory;
import com.ctrip.soa.caravan.util.net.apache.RequestConfigs;
import com.ctrip.soa.caravan.util.serializer.JacksonJsonSerializer;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.net.HttpHeaders;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.SettableFuture;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public final class HttpAsyncRequestExecutors {

    public static <T> ListenableFuture<T> executeJson(CloseableHttpAsyncClient client, String uri, String method, Class<T> clazz) {
        return executeJson(MoreExecutors.directExecutor(), client, uri, method, clazz);
    }

    public static <T> ListenableFuture<T> executeJson(Executor executor, CloseableHttpAsyncClient client, String uri, String method, Class<T> clazz) {
        return executeJson(executor, client, uri, method, null, null, clazz);
    }

    public static <T> ListenableFuture<T> executeJson(CloseableHttpAsyncClient client, String uri, Object data, Class<T> clazz) {
        return executeJson(MoreExecutors.directExecutor(), client, uri, data, clazz);
    }

    public static <T> ListenableFuture<T> executeJson(Executor executor, CloseableHttpAsyncClient client, String uri, Object data, Class<T> clazz) {
        return executeJson(executor, client, uri, HttpPost.METHOD_NAME, null, null, data, clazz);
    }

    public static <T> ListenableFuture<T> executeJson(CloseableHttpAsyncClient client, String uri, String method, ListMultimap<String, String> headers,
            RequestConfig config, Class<T> clazz) {
        return executeJson(MoreExecutors.directExecutor(), client, uri, method, headers, config, clazz);
    }

    public static <T> ListenableFuture<T> executeJson(Executor executor, CloseableHttpAsyncClient client, String uri, String method,
            ListMultimap<String, String> headers, RequestConfig config, Class<T> clazz) {
        return executeJson(executor, client, uri, method, headers, config, null, clazz);
    }

    public static <T> ListenableFuture<T> executeJson(CloseableHttpAsyncClient client, String uri, String method, ListMultimap<String, String> headers,
            RequestConfig config, Object data, Class<T> clazz) {
        return executeJson(MoreExecutors.directExecutor(), client, uri, method, headers, config, data, clazz);
    }

    public static <T> ListenableFuture<T> executeJson(Executor executor, CloseableHttpAsyncClient client, String uri, String method,
            ListMultimap<String, String> headers, RequestConfig config, Object data, Class<T> clazz) {
        return execute(executor, client, uri, method, headers, config, data, JacksonJsonSerializer.INSTANCE, clazz);
    }

    public static <T> ListenableFuture<T> execute(Executor executor, CloseableHttpAsyncClient client, String uri, String method,
            ListMultimap<String, String> headers, RequestConfig config, StreamSerializer serializer, Class<T> clazz) {
        return execute(executor, client, uri, method, headers, config, null, serializer, clazz);
    }

    public static <T> ListenableFuture<T> execute(CloseableHttpAsyncClient client, String uri, String method, ListMultimap<String, String> headers,
            RequestConfig config, StreamSerializer serializer, Class<T> clazz) {
        return execute(client, uri, method, headers, config, null, serializer, clazz);
    }

    public static <T> ListenableFuture<T> execute(CloseableHttpAsyncClient client, String uri, String method, ListMultimap<String, String> headers,
            RequestConfig config, Object data, StreamSerializer serializer, Class<T> clazz) {
        return execute(MoreExecutors.directExecutor(), client, uri, method, headers, config, data, serializer, clazz);
    }

    public static <T> ListenableFuture<T> execute(Executor executor, CloseableHttpAsyncClient client, String uri, String method,
            ListMultimap<String, String> headers, RequestConfig config, Object data, StreamSerializer serializer, Class<T> clazz) {
        config = RequestConfigs.createCascadedRequestConfig(client, config);

        if (serializer != null) {
            if (headers == null)
                headers = ArrayListMultimap.create();
            if (!headers.containsKey(HttpHeaders.CONTENT_TYPE))
                headers.put(HttpHeaders.CONTENT_TYPE, serializer.contentType());
            if (!headers.containsKey(HttpHeaders.ACCEPT))
                headers.put(HttpHeaders.ACCEPT, serializer.contentType());
        }

        HttpUriRequest request = HttpRequestFactory.createRequest(uri, method, headers, config, data, serializer, false);
        return execute(executor, client, request, serializer, clazz);
    }

    public static <T> ListenableFuture<T> execute(CloseableHttpAsyncClient client, HttpUriRequest request, StreamSerializer serializer, Class<T> clazz) {
        return execute(MoreExecutors.directExecutor(), client, request, serializer, clazz);
    }

    public static <T> ListenableFuture<T> execute(Executor executor, CloseableHttpAsyncClient client, HttpUriRequest request, StreamSerializer serializer,
            Class<T> clazz) {
        NullArgumentChecker.DEFAULT.check(executor, "executor");
        ListenableFuture<HttpResponse> httpResponseFuture = execute(client, request);
        return Futures.transformAsync(httpResponseFuture, new HttpResponseTransformer<>(serializer, clazz), executor);
    }

    public static ListenableFuture<HttpResponse> execute(CloseableHttpAsyncClient client, HttpUriRequest request) {
        final SettableFuture<HttpResponse> future = SettableFuture.create();
        client.execute(request, new FutureCallback<HttpResponse>() {
            @Override
            public void completed(HttpResponse result) {
                future.set(result);
            }

            @Override
            public void failed(Exception ex) {
                future.setException(ex);
            }

            @Override
            public void cancelled() {
                Exception ex = new InterruptedException("Request has been cancelled.");
                future.setException(ex);
            }
        });
        return future;
    }

    private HttpAsyncRequestExecutors() {

    }

}
