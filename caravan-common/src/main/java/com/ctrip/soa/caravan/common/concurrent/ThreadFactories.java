package com.ctrip.soa.caravan.common.concurrent;

import java.util.concurrent.ThreadFactory;

import com.ctrip.soa.caravan.common.value.StringValues;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public final class ThreadFactories {

    public static final ThreadFactory DEFAULT = newDaemonThreadFactory(null);

    private ThreadFactories() {

    }

    public static ThreadFactory newDaemonThreadFactory(String nameFormat) {
        ThreadFactoryBuilder builder = new ThreadFactoryBuilder();
        builder.setDaemon(true);
        if (!StringValues.isNullOrWhitespace(nameFormat))
            builder.setNameFormat(nameFormat);
        return builder.build();
    }

}
