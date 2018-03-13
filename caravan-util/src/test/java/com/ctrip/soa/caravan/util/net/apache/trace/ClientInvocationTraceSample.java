package com.ctrip.soa.caravan.util.net.apache.trace;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ctrip.soa.caravan.common.concurrent.Threads;
import com.ctrip.soa.caravan.util.net.apache.HttpRequestExecutors;
import com.ctrip.soa.caravan.util.net.apache.PoolingHttpClientFactory;
import com.ctrip.soa.caravan.util.net.apache.trace.TracingManagedHttpClientConnectionFactory;
import com.ctrip.soa.caravan.util.serializer.JacksonJsonSerializer;

import com.sun.net.httpserver.*;

/**
 * Created by Qiang Zhao on 10/25/2017.
 */
public class ClientInvocationTraceSample {

    private static final Logger _logger = LoggerFactory.getLogger(ClientInvocationTraceSample.class);

    public static final String LOCAL_SERVICE_URL = "http://localhost:8000/test";
    public static final String LOCAL_SERVICE_URL2 = "http://localhost:8080/test-service/test2/json/getitems?take=1&validationString=111111";

    private PoolingHttpClientConnectionManager connectionManager;
    private CloseableHttpClient httpClient;

    public ClientInvocationTraceSample() {
        connectionManager = new PoolingHttpClientConnectionManager(getConnectionFactory());
        httpClient = PoolingHttpClientFactory.create(null, null, connectionManager);
    }

    protected TracingManagedHttpClientConnectionFactory getConnectionFactory() {
        return TracingManagedHttpClientConnectionFactory.DEFAULT;
    }

    @Test
    public void testClient() throws IOException {
        String url = "http://localhost:8080/test-client/java/";
        for (int i = 0; i < 10 * 1000; i++) {
            String responseData = HttpRequestExecutors.executeString(httpClient, url, HttpGet.METHOD_NAME, null, null, null, null, false);
            Threads.sleep(5);
            System.out.println(responseData.length());
            Threads.sleep(5);
        }
    }

    @Test
    public void request() throws IOException {
        startServer();
        Threads.sleep(100);

        System.out.println();
        System.out.println("Content Length Encoding");
        System.out.println();
        for (int i = 0; i < 2; i++)
            doRequest();

        System.out.println();
        System.out.println("Trunked Transfer Encoding");
        System.out.println();

        for (int i = 0; i < 2; i++)
            doRequest2();
    }

    private void doRequest() {
        _logger.info("request starting...");
        String responseData = HttpRequestExecutors.executeString(httpClient, LOCAL_SERVICE_URL, HttpPost.METHOD_NAME, null, null, new Object(),
                JacksonJsonSerializer.INSTANCE, false);
        _logger.info("Response End");
        _logger.info("Response Data: \n" + responseData);
    }

    private void doRequest2() {
        _logger.info("request starting...");
        Response responseData = HttpRequestExecutors.executeJson(httpClient, LOCAL_SERVICE_URL2, new Object(), Response.class);
        _logger.info("Response End");
        _logger.info("Response Data: \n" + responseData);
    }

    @SuppressWarnings("restriction")
    private void startServer() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/test", new MyHandler());
        server.start();
        _logger.info("server started");
    }

    @SuppressWarnings("restriction")
    private static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            Headers headers = t.getResponseHeaders();
            for (int i = 0; i < 10; i++)
                headers.set("OK" + i, "t1" + i);

            String response = "This is the response";
            t.sendResponseHeaders(200, response.length());

            Threads.sleep(50);
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

}

class Response {
    public String ok;
}
