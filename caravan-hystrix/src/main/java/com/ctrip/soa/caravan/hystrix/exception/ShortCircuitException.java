package com.ctrip.soa.caravan.hystrix.exception;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class ShortCircuitException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ShortCircuitException() {

    }

    public ShortCircuitException(String message) {
        super(message);
    }

    public ShortCircuitException(String message, Exception cause) {
        super(message, cause);
    }

}
