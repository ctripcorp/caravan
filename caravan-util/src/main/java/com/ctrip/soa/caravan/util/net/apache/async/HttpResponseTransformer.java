package com.ctrip.soa.caravan.util.net.apache.async;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import com.ctrip.soa.caravan.common.serializer.StreamSerializer;
import com.ctrip.soa.caravan.common.value.checker.NullArgumentChecker;
import com.google.common.util.concurrent.AsyncFunction;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class HttpResponseTransformer<T> implements AsyncFunction<HttpResponse, T> {

    private StreamSerializer _serializer;
    private Class<T> _clazz;

    public HttpResponseTransformer(StreamSerializer serializer, Class<T> clazz) {
        NullArgumentChecker.DEFAULT.check(serializer, "serializer");
        NullArgumentChecker.DEFAULT.check(clazz, "clazz");
        _serializer = serializer;
        _clazz = clazz;
    }

    @Override
    public ListenableFuture<T> apply(HttpResponse response) throws Exception {
        SettableFuture<T> future = SettableFuture.create();
        try {
            T result = deserialize(response);
            future.set(result);
        } catch (Throwable ex) {
            future.setException(ex);
        } finally {
            close(response);
        }

        return future;
    }

    protected T deserialize(HttpResponse response) throws UnsupportedOperationException, IOException {
        return _serializer.deserialize(response.getEntity().getContent(), _clazz);
    }

    protected void close(HttpResponse response) {
        if (response == null)
            return;

        HttpEntity entity = response.getEntity();
        if (entity != null)
            EntityUtils.consumeQuietly(entity);
    }

}
