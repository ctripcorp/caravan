package com.ctrip.soa.caravan.common.trace;

import java.util.Map;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public final class NullTrace implements Trace {

    public static final Trace INSTANCE = new NullTrace();

    private NullTrace() {

    }

    @Override
    public void start() {

    }

    @Override
    public void markEvent(String message) {

    }

    @Override
    public void markEvent(String message, Map<String, String> data) {

    }

    @Override
    public void markSuccess() {

    }

    @Override
    public void markSuccess(String message) {

    }

    @Override
    public void markSuccess(String message, Map<String, String> data) {

    }

    @Override
    public void markFail() {

    }

    @Override
    public void markFail(String message) {

    }

    @Override
    public void markFail(String message, Map<String, String> data) {

    }

    @Override
    public void markFail(Throwable ex) {

    }

    @Override
    public void markFail(Throwable ex, Map<String, String> data) {

    }

    @Override
    public void markFail(String message, Throwable ex) {

    }

    @Override
    public void markFail(String message, Throwable ex, Map<String, String> data) {

    }

    @Override
    public void end() {

    }

}
