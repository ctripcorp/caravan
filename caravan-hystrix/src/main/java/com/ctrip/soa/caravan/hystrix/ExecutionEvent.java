package com.ctrip.soa.caravan.hystrix;

public enum ExecutionEvent {

    SUCCESS, VALIDATION_FAIL, FAILED, TIMEOUT, SHORT_CIRCUITED, REJECTED

}
