package com.ctrip.soa.caravan.util.net.apache.trace;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.config.MessageConstraints;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.ContentLengthStrategy;
import org.apache.http.impl.BHttpConnectionBase;
import org.apache.http.impl.DefaultBHttpClientConnection;
import org.apache.http.impl.conn.DefaultHttpResponseParserFactory;
import org.apache.http.impl.conn.DefaultManagedHttpClientConnection;
import org.apache.http.impl.io.HttpTransportMetricsImpl;
import org.apache.http.impl.io.SessionInputBufferImpl;
import org.apache.http.io.HttpMessageParser;
import org.apache.http.io.HttpMessageParserFactory;
import org.apache.http.io.HttpMessageWriterFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.Socket;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

/**
 * Created by Qiang Zhao on 10/25/2017.
 */
public class TracingManagedHttpClientConnection extends DefaultManagedHttpClientConnection {

    private static final Logger _logger = LoggerFactory.getLogger(TracingManagedHttpClientConnection.class);

    private static Field _sessionInputBufferField;
    private static Field _responseParserField;
    private static boolean _hackFiledGot;

    private LogFunc _logFunc;

    TracingSessionInputBufferImpl _sessionInputBufferImpl;

    static {
        try {
            _sessionInputBufferField = BHttpConnectionBase.class.getDeclaredField("inbuffer");
            _sessionInputBufferField.setAccessible(true);
            _logger.info("Found the inbuffer field from base classes of BHttpConnectionBase");

            _responseParserField = DefaultBHttpClientConnection.class.getDeclaredField("responseParser");
            _responseParserField.setAccessible(true);
            _logger.info("Found the responseParser field from base classes of DefaultBHttpClientConnection");

            _hackFiledGot = true;
        } catch (Throwable ex) {
            _logger.warn("Cannot find the hack fields", ex);
        }
    }

    public TracingManagedHttpClientConnection(final String id, final int buffersize, final int fragmentSizeHint, final CharsetDecoder chardecoder,
            final CharsetEncoder charencoder, final MessageConstraints constraints, final ContentLengthStrategy incomingContentStrategy,
            final ContentLengthStrategy outgoingContentStrategy, final HttpMessageWriterFactory<HttpRequest> requestWriterFactory,
            final HttpMessageParserFactory<HttpResponse> responseParserFactory, LogFunc logFunc) {
        super(id, buffersize, fragmentSizeHint, chardecoder, charencoder, constraints, incomingContentStrategy, outgoingContentStrategy, requestWriterFactory,
                responseParserFactory);

        _logFunc = logFunc;

        hackBufferFields(buffersize, constraints, chardecoder, responseParserFactory);
    }

    private void hackBufferFields(int buffersize, MessageConstraints messageConstraints, CharsetDecoder chardecoder,
            HttpMessageParserFactory<HttpResponse> responseParserFactory) {
        if (!_hackFiledGot)
            return;

        try {
            SessionInputBufferImpl old = (SessionInputBufferImpl) _sessionInputBufferField.get(this);
            _sessionInputBufferImpl = new TracingSessionInputBufferImpl((HttpTransportMetricsImpl) old.getMetrics(), buffersize, -1,
                    messageConstraints != null ? messageConstraints : MessageConstraints.DEFAULT, chardecoder, _logFunc);
            _sessionInputBufferField.set(this, _sessionInputBufferImpl);

            HttpMessageParser<HttpResponse> responseParser = (responseParserFactory != null ? responseParserFactory : DefaultHttpResponseParserFactory.INSTANCE)
                    .create(getSessionInputBuffer(), messageConstraints);
            _responseParserField.set(this, responseParser);
        } catch (Exception ex) {
            _logger.warn("Hack fields failed.", ex);
        }
    }

    public void close() throws IOException {
        super.close();
    }

    @Override
    public void setSocketTimeout(final int timeout) {
        super.setSocketTimeout(timeout);
    }

    @Override
    public void shutdown() throws IOException {
        super.shutdown();
    }

    @Override
    protected InputStream getSocketInputStream(final Socket socket) throws IOException {
        InputStream in = super.getSocketInputStream(socket);
        return in;
    }

    @Override
    protected OutputStream getSocketOutputStream(final Socket socket) throws IOException {
        OutputStream out = super.getSocketOutputStream(socket);
        return out;
    }

    @Override
    protected void onResponseReceived(final HttpResponse response) {

    }

    @Override
    protected void onRequestSubmitted(final HttpRequest request) {

    }

    @Override
    public void sendRequestHeader(final HttpRequest request) throws HttpException, IOException {
        _logFunc.log("send request header start", "sendRequestHeaderStart");
        super.sendRequestHeader(request);
        _logFunc.log("send request header end", "sendRequestHeaderEnd");
    }

    @Override
    public void sendRequestEntity(final HttpEntityEnclosingRequest request) throws HttpException, IOException {
        _logFunc.log("send request body start", "sendRequestBodyStart");
        super.sendRequestEntity(request);
        _logFunc.log("send request body end", "sendRequestBodyEnd");
    }

    @Override
    public HttpResponse receiveResponseHeader() throws HttpException, IOException {
        try {
            _logFunc.log("receive response header start", "receiveResponseHeaderStart");
            return super.receiveResponseHeader();
        } finally {
            _logFunc.log("receive response header end", "receiveResponseHeaderEnd");
            _sessionInputBufferImpl.resetFirstRead();
        }
    }

    @Override
    public void receiveResponseEntity(final HttpResponse response) throws HttpException, IOException {
        _logFunc.log("receive response body start", "receiveResponseBodyStart");
        super.receiveResponseEntity(response);

        HttpEntity entity = response.getEntity();
        if (entity != null && entity.getContent() != null) {
            ((BasicHttpEntity) entity).setContent(new TracingInputStream(entity.getContent(), _logFunc));
        }
    }

}
