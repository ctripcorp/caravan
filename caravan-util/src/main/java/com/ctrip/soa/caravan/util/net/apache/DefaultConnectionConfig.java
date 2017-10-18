package com.ctrip.soa.caravan.util.net.apache;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public final class DefaultConnectionConfig {

    public static final int DEFAULT_MAX_CONNECTIONS_PER_ROUTE = 10 * 1000;
    public static final int DEFAULT_MAX_TOTAL_CONNECTIONS = 200 * 1000;
    public static final int DEFAULT_CONNECTION_TTL = 5 * 60 * 1000;
    public static final int DEFAULT_INACTIVITY_TIME_BEFORE_VALIDATE = 10 * 1000;
    public static final int DEFAULT_CONNECTION_IDLE_TIME = 30 * 1000;
    public static final int DEFAULT_CLEAN_CHECK_INTERVAL = 5 * 1000;

    private DefaultConnectionConfig() {

    }

}
