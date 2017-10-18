package com.ctrip.soa.caravan.hystrix;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public interface ExecutionContext {

    void startExecution();

    boolean tryStartExecution();

    void markSuccess();

    void markFail();

    void markValidationFail();

    void endExecution();

    ExecutionEvent executionEvent();

}
