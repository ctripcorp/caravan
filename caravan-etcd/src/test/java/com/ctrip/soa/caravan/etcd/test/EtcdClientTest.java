package com.ctrip.soa.caravan.etcd.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Ignore;
import org.junit.Test;
import com.ctrip.soa.caravan.common.concurrent.Threads;
import com.ctrip.soa.caravan.configuration.ConfigurationSource;
import com.ctrip.soa.caravan.configuration.facade.ConfigurationManagers;
import com.ctrip.soa.caravan.configuration.facade.TypedDynamicCachedCorrectedProperties;
import com.ctrip.soa.caravan.configuration.typed.dynamic.cached.corrected.TypedDynamicCachedCorrectedConfigurationManager;
import com.ctrip.soa.caravan.etcd.v2.EtcdClient;
import com.ctrip.soa.caravan.etcd.v2.EtcdClientConfig;
import com.ctrip.soa.caravan.util.net.apache.DynamicPoolingHttpClientProvider;
import com.ctrip.soa.caravan.util.net.apache.HttpRequestException;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
@Ignore
public class EtcdClientTest {

    private static EtcdClient _client;
    private static final String ETCD_SERVICE_URL = "etcd url";

    static {
        TypedDynamicCachedCorrectedConfigurationManager manager = ConfigurationManagers
                .newTypedDynamicCachedCorrectedManager(new ArrayList<>().toArray(new ConfigurationSource[0]));
        _client = new EtcdClient(new EtcdClientConfig(ETCD_SERVICE_URL,
                new DynamicPoolingHttpClientProvider("caravan-etcd", new TypedDynamicCachedCorrectedProperties(manager))));
    }

    @Test
    public void etcdTestPut() {
        String keyValue = "lst1234";
        try {
            _client.put(keyValue, keyValue);
            assertEquals(keyValue, _client.get(keyValue));
        } finally {
            _client.delete(keyValue);
        }
    }

    /*
     * 由于key-lst999超过ttl 所以client在获取lst999时会抛出java.lang.RuntimeException
     */
    @Test
    public void etcdTestPutTtl() {
        String keyValue = "lst9999";
        try {
            _client.put(keyValue, keyValue, 3L);
            assertEquals(keyValue, _client.get(keyValue));

            Threads.sleep(3100);
            assertEquals(null, _client.get(keyValue));
        } finally {
            _client.delete(keyValue);
        }
    }

    /*
     * 由于key-lst888已经被删除 所以client在获取lst888时会抛出java.lang.RuntimeException
     */
    @Test
    public void etcdTestDelete() {
        String keyValue = "lst888";
        try {
            _client.put(keyValue, keyValue);
            assertEquals(keyValue, _client.get(keyValue));
            _client.delete(keyValue);
            assertEquals(null, _client.get(keyValue));
        } finally {
            _client.delete(keyValue);
        }
    }

    @Test
    public void etcdTestPutStr() {
        String keyValue = "lst12";
        try {
            _client.put(keyValue, keyValue, 10l, false);
            assertEquals(keyValue, _client.get(keyValue));
        } finally {
            _client.delete(keyValue);
        }
    }

    /*
     * 这里会抛出403 Forbidden
     */
    @Test
    public void etcdTestcreateDirWithOutDelete() {
        String keyValue = "test1239657928";
        try {
            _client.createDir(keyValue);
            try {
                _client.createDir(keyValue);
            } catch (HttpRequestException e) {
                assertEquals(403, e.statusCode());
            }
        } finally {
            _client.deleteDir(keyValue, true);
        }
    }

    /*
     * 这里创建dir后进行delete
     */
    @Test
    public void etcdTestcreateDir() {
        String keyValue = "test654321";
        try {
            _client.createDir(keyValue);
        } finally {
            _client.deleteDir(keyValue, true);
        }
    }

    /*
     * 由于ttl时间未到，创建dir会抛出403 Forbidden
     */
    @Test
    public void createDirWithTtl() {
        String keyValue = "test7654321456";
        try {
            _client.createDir(keyValue, 3l);
            try {
                _client.createDir(keyValue);
                fail();
            } catch (HttpRequestException e) {
                assertEquals(403, e.statusCode());
            }
        } finally {
            _client.deleteDir(keyValue, true);
        }
    }

    @Test
    public void createDirWithTtlAndPassed() {
        String keyValue = "test87654321";
        try {
            _client.createDir(keyValue, 3l);
            Threads.sleep(3100);
            _client.createDir(keyValue);
        } finally {
            _client.deleteDir(keyValue, true);
        }
    }

    /*
     * test1234324122——dir不存在 这里会抛出404 Not Found
     */
    @Test
    public void ListDirNode() {
        String keyValue = "test1234324122";
        try {
            _client.listDir(keyValue);
            fail();
        } catch (HttpRequestException e) {
            assertEquals(404, e.statusCode());
        }
    }

    @Test
    public void deleteKeyWithoutHaving() {
        String keyValue = "kkk12335447";
        _client.delete(keyValue);
    }

    /*
     * 创建各种特殊字符的key——可以正常创建
     */
    @Test
    public void createKey() {
        String keyValue = "!@#$%^&*()";
        try {
            _client.put(keyValue, keyValue);
            assertEquals(_client.get(keyValue), keyValue);
        } finally {
            _client.delete(keyValue);
        }
    }

    /*
     * 创建各种特殊字符的dir——可以正常创建
     */
    @Test
    public void createdir() {
        String keyValue = "!@#$%^&*()~";
        try {
            _client.createDir(keyValue);
        } finally {
            _client.deleteDir(keyValue, true);
        }
    }

}
