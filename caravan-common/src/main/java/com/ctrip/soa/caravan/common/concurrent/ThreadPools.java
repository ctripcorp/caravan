package com.ctrip.soa.caravan.common.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public final class ThreadPools {

    private ThreadPools() {

    }

    public static ExecutorService newCachedDaemonThreadPool() {
        return Executors.newCachedThreadPool(ThreadFactories.DEFAULT);
    }

    public static ExecutorService newCachedDaemonThreadPool(String nameFormat) {
        ThreadFactory threadFactory = ThreadFactories.newDaemonThreadFactory(nameFormat);
        return Executors.newCachedThreadPool(threadFactory);
    }

}
