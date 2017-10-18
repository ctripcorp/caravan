package com.ctrip.soa.caravan.etcd.v2;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.message.BasicNameValuePair;

import com.ctrip.soa.caravan.common.value.CollectionValues;
import com.ctrip.soa.caravan.common.value.MapValues;
import com.ctrip.soa.caravan.common.value.StringValues;
import com.ctrip.soa.caravan.common.value.checker.NullArgumentChecker;
import com.ctrip.soa.caravan.common.value.checker.StringArgumentChecker;
import com.ctrip.soa.caravan.util.net.apache.HttpRequestException;
import com.ctrip.soa.caravan.util.net.apache.HttpRequestExecutors;
import com.ctrip.soa.caravan.util.serializer.JacksonJsonSerializer;
import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class EtcdClient {

    private EtcdClientConfig _clientConfig;
    private String _serviceUrlPrefix;

    public EtcdClient(EtcdClientConfig clientConfig) {
        NullArgumentChecker.DEFAULT.check(clientConfig, "clientConfig");
        _clientConfig = clientConfig;
        _serviceUrlPrefix = StringValues.concatPathParts(clientConfig.etcdServiceUrl(), "v2/keys");
    }

    public String get(String key) {
        StringArgumentChecker.DEFAULT.check(key, "key");

        String requestUrl = generateRequestUrl(key, null);
        HttpGet httpGet = new HttpGet(requestUrl);
        try {
            EtcdResult result = HttpRequestExecutors.execute(_clientConfig.clientProvider().get(), httpGet, JacksonJsonSerializer.INSTANCE, EtcdResult.class);
            checkResult(result);

            return result == null || result.getNode() == null ? null : result.getNode().getValue();
        } catch (HttpRequestException ex) {
            if (ex.statusCode() == 404)
                return null;

            throw ex;
        }
    }

    public void put(String key, String value) {
        put(key, value, null);
    }

    public void put(String key, String value, Long ttl) {
        put(key, value, ttl, null);
    }

    public void put(String key, String value, Long ttl, Boolean prevExist) {
        executePut(key, value, ttl, prevExist, false);
    }

    public void delete(String key) {
        StringArgumentChecker.DEFAULT.check(key, "key");

        String requestUrl = generateRequestUrl(key, null);
        HttpDelete httpDelete = new HttpDelete(requestUrl);
        try {
            EtcdResult result = HttpRequestExecutors.execute(_clientConfig.clientProvider().get(), httpDelete, JacksonJsonSerializer.INSTANCE,
                    EtcdResult.class);
            checkResult(result);
        } catch (HttpRequestException ex) {
            if (ex.statusCode() == 404)
                return;

            throw ex;
        }
    }

    public void createDir(String key) {
        createDir(key, null);
    }

    public void createDir(String key, Long ttl) {
        createDir(key, ttl, null);
    }

    public void createDir(String key, Long ttl, Boolean prevExist) {
        executePut(key, null, ttl, prevExist, true);
    }

    public List<EtcdNode> listDir(String key) throws EtcdClientException {
        return listDir(key, false);
    }

    public List<EtcdNode> listDir(String key, Boolean recursive) throws EtcdClientException {
        StringArgumentChecker.DEFAULT.check(key, "key");

        Map<String, String> params = new HashMap<String, String>();
        params.put("dir", String.valueOf(true));
        if (recursive)
            params.put("recursive", String.valueOf(true));

        String requestUrl = generateRequestUrl(key, params);
        HttpGet httpGet = new HttpGet(requestUrl);
        EtcdResult result = HttpRequestExecutors.execute(_clientConfig.clientProvider().get(), httpGet, JacksonJsonSerializer.INSTANCE, EtcdResult.class);
        checkResult(result);

        if (result == null || result.getNode() == null)
            return null;

        return result.getNode().getNodes();
    }

    public void deleteDir(String key, Boolean recursive) throws EtcdClientException {
        StringArgumentChecker.DEFAULT.check(key, "key");

        Map<String, String> params = new HashMap<String, String>();
        params.put("dir", String.valueOf(true));
        if (recursive)
            params.put("recursive", String.valueOf(true));

        String requestUrl = generateRequestUrl(key, params);
        HttpDelete httpDelete = new HttpDelete(requestUrl);
        try {
            EtcdResult result = HttpRequestExecutors.execute(_clientConfig.clientProvider().get(), httpDelete, JacksonJsonSerializer.INSTANCE,
                    EtcdResult.class);
            checkResult(result);
        } catch (HttpRequestException ex) {
            if (ex.statusCode() == 404)
                return;

            throw ex;
        }
    }

    private void executePut(String key, String value, Long ttl, Boolean prevExist, boolean dir) {
        StringArgumentChecker.DEFAULT.check(key, "key");

        if (!dir)
            StringArgumentChecker.DEFAULT.check(value, "value");

        String requestUrl = generateRequestUrl(key, null);
        HttpPut httpPut = new HttpPut(requestUrl);

        List<BasicNameValuePair> formData = Lists.newArrayList();
        if (!dir)
            formData.add(new BasicNameValuePair("value", value));
        else
            formData.add(new BasicNameValuePair("dir", String.valueOf(true)));

        if (ttl != null) {
            formData.add(new BasicNameValuePair("ttl", String.valueOf(ttl)));
        }

        if (prevExist != null) {
            formData.add(new BasicNameValuePair("prevExist", String.valueOf(prevExist)));
        }

        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formData, Charsets.UTF_8);
        httpPut.setEntity(entity);

        EtcdResult result = HttpRequestExecutors.execute(_clientConfig.clientProvider().get(), httpPut, JacksonJsonSerializer.INSTANCE, EtcdResult.class);
        checkResult(result);
    }

    private String generateRequestUrl(String key, Map<String, String> params) {
        List<String> keyParts = Splitter.on('/').trimResults().omitEmptyStrings().splitToList(key);
        if (CollectionValues.isNullOrEmpty(keyParts))
            throw new IllegalArgumentException("key format is invalid: " + key);

        String rawUrl = _serviceUrlPrefix;
        try {
            for (String part : keyParts) {
                rawUrl = StringValues.concatPathParts(rawUrl, URLEncoder.encode(part, Charsets.UTF_8.name()));
            }
        } catch (Exception ex) {
            throw new IllegalArgumentException("Cannot url encode the key: " + key, ex);
        }

        if (MapValues.isNullOrEmpty(params))
            return rawUrl;

        StringBuffer rawUrlBuf = new StringBuffer(rawUrl).append("?");

        for (String paramKey : params.keySet()) {
            rawUrlBuf.append(paramKey).append("=").append(params.get(paramKey)).append("&");
        }

        return StringValues.trimEnd(rawUrlBuf.toString(), '&');
    }

    private boolean hasErrorCode(EtcdResult result) {
        return result != null && result.getErrorCode() != null;
    }

    private boolean is100Continue(EtcdResult result) {
        return hasErrorCode(result) && result.getErrorCode() == 100;
    }

    private void checkResult(EtcdResult result) {
        if (!hasErrorCode(result))
            return;

        if (is100Continue(result))
            return;

        throw new EtcdClientException(result);
    }

}
