package com.ctrip.soa.caravan.common.trace;

import java.util.Map;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public interface TraceFactory {

    Trace newTrace(String identity);

    Trace newTrace(String identity, Map<String, String> data);

}
