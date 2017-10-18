package com.ctrip.soa.caravan.common.exception;

import java.text.ParseException;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class ParseRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ParseRuntimeException(ParseException ex) {
        super(ex);
    }

}
