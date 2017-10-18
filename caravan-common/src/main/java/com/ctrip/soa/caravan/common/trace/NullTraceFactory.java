package com.ctrip.soa.caravan.common.trace;

import java.util.Map;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public final class NullTraceFactory implements TraceFactory {

    public static final TraceFactory INSTANCE = new NullTraceFactory();

    private NullTraceFactory() {

    }

    @Override
    public Trace newTrace(String identity) {
        return NullTrace.INSTANCE;
    }

    @Override
    public Trace newTrace(String identity, Map<String, String> data) {
        return NullTrace.INSTANCE;
    }

}
