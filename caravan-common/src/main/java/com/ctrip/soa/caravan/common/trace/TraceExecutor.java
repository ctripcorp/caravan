package com.ctrip.soa.caravan.common.trace;

import java.util.Map;

import com.ctrip.soa.caravan.common.delegate.Action;
import com.ctrip.soa.caravan.common.delegate.Func;
import com.ctrip.soa.caravan.common.trace.Trace;
import com.ctrip.soa.caravan.common.value.checker.NullArgumentChecker;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class TraceExecutor {

    private TraceFactory _factory;
    private ThreadLocal<Trace> _currentTrace;

    public TraceExecutor(TraceFactory factory) {
        NullArgumentChecker.DEFAULT.check(factory, "factory");
        _factory = factory;
        _currentTrace = new ThreadLocal<>();
    }

    public <V> V execute(String traceKey, Func<V> executor) {
        Trace trace = _factory.newTrace(traceKey);
        _currentTrace.set(trace);
        try {
            trace.start();

            V value = executor.execute();

            trace.markSuccess();
            return value;
        } catch (Throwable ex) {
            trace.markFail(ex);
            throw ex;
        } finally {
            trace.end();
            _currentTrace.remove();
        }
    }

    public void execute(String traceKey, Action executor) {
        Trace trace = _factory.newTrace(traceKey);
        _currentTrace.set(trace);
        try {
            trace.start();

            executor.execute();

            trace.markSuccess();
        } catch (Throwable ex) {
            trace.markFail(ex);
            throw ex;
        } finally {
            trace.end();
            _currentTrace.remove();
        }
    }

    public void markEvent(String message) {
        Trace trace = _currentTrace.get();
        if (trace == null)
            return;

        trace.markEvent(message);
    }

    public void markEvent(String message, Map<String, String> data) {
        Trace trace = _currentTrace.get();
        if (trace == null)
            return;

        trace.markEvent(message, data);
    }

}
