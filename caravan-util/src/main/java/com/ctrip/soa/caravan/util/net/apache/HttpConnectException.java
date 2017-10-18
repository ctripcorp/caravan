package com.ctrip.soa.caravan.util.net.apache;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class HttpConnectException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public HttpConnectException() {

    }

    public HttpConnectException(String message) {
        super(message);
    }

    public HttpConnectException(Throwable ex) {
        super(ex);
    }

    public HttpConnectException(String message, Throwable ex) {
        super(message, ex);
    }

}
