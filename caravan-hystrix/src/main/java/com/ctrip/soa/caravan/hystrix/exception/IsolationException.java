package com.ctrip.soa.caravan.hystrix.exception;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class IsolationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public IsolationException() {

    }

    public IsolationException(String message) {
        super(message);
    }

    public IsolationException(String message, Exception cause) {
        super(message, cause);
    }

}
