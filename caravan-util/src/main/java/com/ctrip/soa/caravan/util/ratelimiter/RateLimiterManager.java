package com.ctrip.soa.caravan.util.ratelimiter;

import java.util.concurrent.ConcurrentHashMap;

import com.ctrip.soa.caravan.common.delegate.Func;
import com.ctrip.soa.caravan.common.value.ConcurrentHashMapValues;
import com.ctrip.soa.caravan.common.value.StringValues;
import com.ctrip.soa.caravan.common.value.checker.NullArgumentChecker;
import com.ctrip.soa.caravan.common.value.checker.StringArgumentChecker;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class RateLimiterManager {

    private String _managerId;
    private RateLimiterManagerConfig _config;
    private ConcurrentHashMap<String, RateLimiter> _rateLimiterCache;

    public RateLimiterManager(String managerId, RateLimiterManagerConfig config) {
        NullArgumentChecker.DEFAULT.check(managerId, "managerId");
        NullArgumentChecker.DEFAULT.check(config, "config");
        _managerId = managerId;
        _config = config;
        _rateLimiterCache = new ConcurrentHashMap<>();
    }

    public String managerId() {
        return _managerId;
    }

    public RateLimiterManagerConfig config() {
        return _config;
    }

    public RateLimiter getRateLimiter(String rateLimiterId, final RateLimiterConfig rateLimiterConfig) {
        StringArgumentChecker.DEFAULT.check(rateLimiterId, "rateLimiterId");
        NullArgumentChecker.DEFAULT.check(rateLimiterConfig, "rateLimiterConfig");

        final String trimmedRateLimiterId = StringValues.trim(rateLimiterId);
        return ConcurrentHashMapValues.getOrAdd(_rateLimiterCache, trimmedRateLimiterId, new Func<RateLimiter>() {
            @Override
            public RateLimiter execute() {
                return new DefaultRateLimiter(trimmedRateLimiterId, _config.properties(), rateLimiterConfig);
            }
        });
    }

}
