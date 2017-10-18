package com.ctrip.soa.caravan.util.ratelimiter;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public interface RateLimiter {

    String rateLimiterId();

    boolean isRateLimited(String identity);

}
