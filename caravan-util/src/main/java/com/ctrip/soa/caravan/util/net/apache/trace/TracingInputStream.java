package com.ctrip.soa.caravan.util.net.apache.trace;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.impl.io.ChunkedInputStream;
import org.apache.http.impl.io.ContentLengthInputStream;

/**
 * Created by Qiang Zhao on 10/25/2017.
 */
public class TracingInputStream extends FilterInputStream {

    private InputStream _in;
    private boolean _streamAvailableUsable;
    private boolean _availableException;

    private boolean _firstRead;
    private boolean _logResponseEnd;

    private LogFunc _logFunc;

    protected TracingInputStream(InputStream in, LogFunc logFunc) {
        super(in);

        _in = in;
        _streamAvailableUsable = in instanceof ContentLengthInputStream || in instanceof ChunkedInputStream;
        _firstRead = true;
        _logResponseEnd = true;

        _logFunc = logFunc;
    }

    @Override
    public int read() throws IOException {
        int data;
        if (_firstRead) {
            _firstRead = false;

            _logFunc.log("first read body start", "firstReadResponseBodyStart");
            data = super.read();
            _logFunc.log("first read body end", "firstReadResponseBodyEnd");
        } else
            data = super.read();

        logResponseEnd(data);
        return data;
    }

    @Override
    public int read(byte b[], int off, int len) throws IOException {
        int l;

        if (_firstRead) {
            _firstRead = false;

            _logFunc.log("first read body start", "firstReadResponseBodyStart");
            l = super.read(b, off, len);
            _logFunc.log("first read body end", "firstReadResponseBodyEnd");
        } else
            l = super.read(b, off, len);

        logResponseEnd(l, b.length);
        return l;
    }

    private void logResponseEnd(int data) {
        if (_logResponseEnd && data == -1) {
            _logResponseEnd = false;
            _logFunc.log("receive response body end", "receiveResponseBodyEnd");
        }
    }
    
    private void logResponseEnd(int actualLength, int length) {
        if (_logResponseEnd && isUnavailable(actualLength, length)) {
            _logResponseEnd = false;
            _logFunc.log("receive response body end", "receiveResponseBodyEnd");
        }
    }

    private boolean isUnavailable(int actualLength, int length) {
        if (actualLength == -1)
            return true;

        if (actualLength == length)
            return false;

        if (_availableException)
            return false;

        try {
            if (_streamAvailableUsable)
                return _in.available() <= 0;
        } catch (Exception ex) {
            _availableException = true;
        }

        return false;
    }

}
