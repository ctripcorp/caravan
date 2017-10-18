package com.ctrip.soa.caravan.common.serializer;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class SerializationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public SerializationException(Exception ex) {
        super(ex);
    }

    public SerializationException(String message, Exception ex) {
        super(message, ex);
    }

}
