package com.ctrip.soa.caravan.common.value;

import java.io.Closeable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public final class CloseableValues {

    private static final Logger _logger = LoggerFactory.getLogger(CloseableValues.class);

    private CloseableValues() {

    }

    public static void close(Closeable closeable) {
        try {
            if (closeable == null)
                return;

            closeable.close();
        } catch (Throwable ex) {
            _logger.warn("Close closeable object failed", ex);
        }
    }

}
