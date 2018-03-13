package com.ctrip.soa.caravan.util.net.apache.trace;

import java.io.IOException;
import java.nio.charset.CharsetDecoder;

import org.apache.http.config.MessageConstraints;
import org.apache.http.impl.io.HttpTransportMetricsImpl;
import org.apache.http.impl.io.SessionInputBufferImpl;

/**
 * Created by Qiang Zhao on 10/25/2017.
 */
public class TracingSessionInputBufferImpl extends SessionInputBufferImpl {

    private LogFunc _logFunc;

    private boolean _firstRead;

    public TracingSessionInputBufferImpl(final HttpTransportMetricsImpl metrics, final int buffersize, final int minChunkLimit,
            final MessageConstraints constraints, final CharsetDecoder chardecoder, LogFunc logFunc) {
        super(metrics, buffersize, minChunkLimit, constraints, chardecoder);

        _logFunc = logFunc;
        _firstRead = true;
    }

    public TracingSessionInputBufferImpl(final HttpTransportMetricsImpl metrics, final int buffersize, LogFunc logFunc) {
        this(metrics, buffersize, buffersize, null, null, logFunc);
    }

    @Override
    public int fillBuffer() throws IOException {
        if (_firstRead) {
            _firstRead = false;
            _logFunc.log("first read response header start", "firstReadResponseHeaderStart");
            try {
                return super.fillBuffer();
            } finally {
                _logFunc.log("first read response header end", "firstReadResponseHeaderEnd");
            }
        }

        return super.fillBuffer();
    }

    public void resetFirstRead() {
        _firstRead = true;
    }

}
