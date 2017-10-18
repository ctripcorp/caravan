package com.ctrip.soa.caravan.util.net.apache;

import java.io.IOException;
import java.util.Comparator;
import java.util.concurrent.ConcurrentSkipListSet;

import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ctrip.soa.caravan.common.value.ArrayValues;
import com.ctrip.soa.caravan.common.value.corrector.RangeCorrector;
import com.google.common.base.Objects;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class IOExceptionRetryHelper {

    private static final Logger _logger = LoggerFactory.getLogger(IOExceptionRetryHelper.class);
    private static final RangeCorrector<Integer> _retryTimesRangeCorrector = new RangeCorrector<Integer>(1, 10);

    public static final IOExceptionRetryHelper DEFAULT = new DefaultIOExceptionRetryHelper();

    private volatile boolean _enabled = true;

    private int _retryTimes;

    private ConcurrentSkipListSet<Class<?>> _ioExceptionTypesToBeRetried = new ConcurrentSkipListSet<>(
            new Comparator<Class<?>>() {
                @Override
                public int compare(Class<?> o1, Class<?> o2) {
                    if (Objects.equal(o1, o2))
                        return 0;
                    if (o1 == null)
                        return -1;
                    if (o2 == null)
                        return 1;
                    return o1.getName().compareTo(o2.getName());
                }
            });

    private HttpRequestRetryHandler _retryHandler = new HttpRequestRetryHandler() {
        @Override
        public boolean retryRequest(IOException ex, int executionCount, HttpContext context) {
            if (!IOExceptionRetryHelper.this.isEnabled())
                return false;

            if (executionCount > IOExceptionRetryHelper.this.getRetryTimes())
                return false;

            if (!IOExceptionRetryHelper.this.isRetriableIOException(ex))
                return false;

            _logger.info("Will retry. Execution count: {}, exception type: {}, exception message: {}", executionCount,
                    ex.getClass().getName(), ex.getMessage());
            return true;
        }
    };

    public IOExceptionRetryHelper(Class<?>... ioExceptionClasses) {
        this(1, ioExceptionClasses);
    }

    public IOExceptionRetryHelper(int retryTimes, Class<?>... ioExceptionClasses) {
        _retryTimes = _retryTimesRangeCorrector.correct(retryTimes);
        addIOExceptionsToBeRetried(ioExceptionClasses);
    }

    public void enable() {
        _enabled = true;
    }

    public void disable() {
        _enabled = false;
    }

    public boolean isEnabled() {
        return _enabled;
    }

    public int getRetryTimes() {
        return _retryTimes;
    }

    public void setRetryTimes(int retryTimes) {
        _retryTimes = _retryTimesRangeCorrector.correct(retryTimes);
    }

    public HttpRequestRetryHandler retryHandler() {
        return _retryHandler;
    }

    public void addIOExceptionsToBeRetried(Class<?>... ioExceptionClasses) {
        if (ArrayValues.isNullOrEmpty(ioExceptionClasses))
            return;

        for (Class<?> c : ioExceptionClasses) {
            if (c == null)
                continue;

            if (!IOException.class.isAssignableFrom(c)) {
                throw new IllegalArgumentException("Class " + c.getName() + " is not subclass of IOException");
            }

            _ioExceptionTypesToBeRetried.add(c);
        }
    }

    protected void removeIOExceptionsToBeRetried(Class<?>... ioExceptionClasses) {
        if (ArrayValues.isNullOrEmpty(ioExceptionClasses))
            return;

        for (Class<?> c : ioExceptionClasses) {
            if (c == null)
                continue;

            _ioExceptionTypesToBeRetried.remove(c);
        }
    }

    protected void clearIOExceptionsToBeRetried() {
        _ioExceptionTypesToBeRetried.clear();
    }

    protected boolean isRetriableIOException(IOException ex) {
        if (ex == null)
            return false;

        for (Class<?> c : _ioExceptionTypesToBeRetried) {
            if (c.isInstance(ex)) {
                return true;
            }
        }

        return false;
    }

    private static class DefaultIOExceptionRetryHelper extends IOExceptionRetryHelper {

        private boolean _inited;

        public DefaultIOExceptionRetryHelper() {
            super(NoHttpResponseException.class);
            _inited = true;
        }

        @Override
        public void enable() {
            checkDisabled();
            super.enable();
        }

        @Override
        public void disable() {
            checkDisabled();
            super.disable();
        }

        @Override
        public void setRetryTimes(int retryTimes) {
            checkDisabled();
            super.setRetryTimes(retryTimes);
        }

        @Override
        public void addIOExceptionsToBeRetried(Class<?>... ioExceptionClasses) {
            checkDisabled();
            super.addIOExceptionsToBeRetried(ioExceptionClasses);
        }

        @Override
        protected void removeIOExceptionsToBeRetried(Class<?>... ioExceptionClasses) {
            checkDisabled();
            super.removeIOExceptionsToBeRetried(ioExceptionClasses);
        }

        @Override
        protected void clearIOExceptionsToBeRetried() {
            checkDisabled();
            super.clearIOExceptionsToBeRetried();
        }

        private void checkDisabled() {
            if (_inited)
                throw new UnsupportedOperationException("DEFAULT retry helper cannot be changed.");
        }

    }

}
