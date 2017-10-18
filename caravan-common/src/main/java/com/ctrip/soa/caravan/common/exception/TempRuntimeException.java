package com.ctrip.soa.caravan.common.exception;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class TempRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public TempRuntimeException(Exception ex) {
        super(ex);
    }

}
