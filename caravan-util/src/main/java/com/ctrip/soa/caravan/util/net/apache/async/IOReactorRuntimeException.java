package com.ctrip.soa.caravan.util.net.apache.async;

import org.apache.http.nio.reactor.IOReactorException;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class IOReactorRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public IOReactorRuntimeException(IOReactorException ex) {
        super(ex);
    }

}
