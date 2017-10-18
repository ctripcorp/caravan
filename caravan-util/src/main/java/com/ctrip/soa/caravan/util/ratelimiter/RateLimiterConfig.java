package com.ctrip.soa.caravan.util.ratelimiter;

import com.ctrip.soa.caravan.common.concurrent.collect.circularbuffer.timebucket.TimeBufferConfig;
import com.ctrip.soa.caravan.common.value.checker.NullArgumentChecker;
import com.ctrip.soa.caravan.configuration.util.RangePropertyConfig;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class RateLimiterConfig {

    public static final String ENABLED_PROPERTY_KEY = "rate-limiter.enabled";
    public static final String DEFAULT_RATE_LIMIT_PROPERTY_KEY = "rate-limiter.default-rate-limit";
    public static final String RATE_LIMIT_MAP_PROPERTY_KEY = "rate-limiter.rate-limit-map";

    private boolean _enabled;
    private RangePropertyConfig<Long> _rateLimitPropertyConfig;
    private TimeBufferConfig _bufferConfig;

    public RateLimiterConfig(boolean enabled, RangePropertyConfig<Long> rateLimitPropertyConfig, TimeBufferConfig bufferConfig) {
        NullArgumentChecker.DEFAULT.check(rateLimitPropertyConfig, "rateLimitPropertyConfig");
        NullArgumentChecker.DEFAULT.check(bufferConfig, "bufferConfig");

        _enabled = enabled;
        _rateLimitPropertyConfig = rateLimitPropertyConfig;
        _bufferConfig = bufferConfig;
    }

    public boolean enabled() {
        return _enabled;
    }

    public RangePropertyConfig<Long> rateLimitPropertyConfig() {
        return _rateLimitPropertyConfig;
    }

    public TimeBufferConfig bufferConfig() {
        return _bufferConfig;
    }

}
