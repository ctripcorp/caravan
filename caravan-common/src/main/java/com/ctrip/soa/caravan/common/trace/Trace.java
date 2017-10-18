package com.ctrip.soa.caravan.common.trace;

import java.util.Map;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public interface Trace {

    void start();

    void markEvent(String message);

    void markEvent(String message, Map<String, String> data);

    void markSuccess();

    void markSuccess(String message);

    void markSuccess(String message, Map<String, String> data);

    void markFail();

    void markFail(String message);

    void markFail(String message, Map<String, String> data);

    void markFail(Throwable ex);

    void markFail(Throwable ex, Map<String, String> data);

    void markFail(String message, Throwable ex);

    void markFail(String message, Throwable ex, Map<String, String> data);

    void end();

}
