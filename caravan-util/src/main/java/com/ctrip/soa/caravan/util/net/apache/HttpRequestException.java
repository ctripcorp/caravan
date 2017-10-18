package com.ctrip.soa.caravan.util.net.apache;

import java.io.InputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;

import com.ctrip.soa.caravan.common.value.checker.NullArgumentChecker;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class HttpRequestException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private int _statusCode;
    private String _reasonPhrase;
    private ProtocolVersion _protocolVersion;
    private Header[] _responseHeaders;
    private String _responseBody;

    public HttpRequestException(CloseableHttpResponse response) {
        super(toErrorMessage(response));

        StatusLine statusLine = response.getStatusLine();
        if (statusLine == null)
            return;

        _statusCode = statusLine.getStatusCode();
        _reasonPhrase = statusLine.getReasonPhrase();
        _protocolVersion = statusLine.getProtocolVersion();
        _responseHeaders = response.getAllHeaders();

        HttpEntity entity = response.getEntity();
        if (entity == null)
            return;

        try (InputStream is = entity.getContent();) {
            _responseBody = EntityUtils.toString(entity);
        } catch (Throwable ex) {

        }
    }

    public int statusCode() {
        return _statusCode;
    }

    public String reasonPhrase() {
        return _reasonPhrase;
    }

    public ProtocolVersion protocolVersion() {
        return _protocolVersion;
    }

    public Header[] responseHeaders() {
        return _responseHeaders;
    }

    public String responseBody() {
        return _responseBody;
    }

    private static String toErrorMessage(CloseableHttpResponse response) {
        NullArgumentChecker.DEFAULT.check(response, "response");

        StatusLine statusLine = response.getStatusLine();
        if (statusLine == null)
            return "Http request failed. status line is null.";

        return String.format("Http request failed. status code: %s, reason phrase: %s, protocol version: %s", statusLine.getStatusCode(),
                statusLine.getReasonPhrase(), statusLine.getProtocolVersion());
    }

}
