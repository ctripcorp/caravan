package com.ctrip.soa.caravan.util.net.apache.trace;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Qiang Zhao on 10/25/2017.
 */
public class DefaultLogFunc implements LogFunc {

    private static final Logger _logger = LoggerFactory.getLogger(DefaultLogFunc.class);
    
    public static final DefaultLogFunc DEFAULT = new DefaultLogFunc();

    @Override
    public void log(String message, String... args) {
        _logger.info(message);
    }

}
