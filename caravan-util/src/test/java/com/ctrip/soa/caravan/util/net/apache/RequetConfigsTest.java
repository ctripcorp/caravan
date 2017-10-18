package com.ctrip.soa.caravan.util.net.apache;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.junit.Assert;
import org.junit.Test;

import com.ctrip.soa.caravan.util.net.apache.async.PoolingHttpAsyncClientFactory;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class RequetConfigsTest {

    @Test
    public void getRequestConfigFromClient()  {
        CloseableHttpClient client = PoolingHttpClientFactory.create();
        RequestConfig requestConfig = RequestConfigs.getRequestConfig(client);
        Assert.assertNotNull(requestConfig);
    }
    
    @Test
    public void getRequestConfigFromAsyncClient()  {
        CloseableHttpAsyncClient client = PoolingHttpAsyncClientFactory.create();
        RequestConfig requestConfig = RequestConfigs.getRequestConfig(client);
        Assert.assertNotNull(requestConfig);
    }
    
    @Test
    public void getRequestConfigFromObject() {
        RequestConfig requestConfig = RequestConfigs.getRequestConfig(new Object());
        Assert.assertNull(requestConfig);
    }

}
