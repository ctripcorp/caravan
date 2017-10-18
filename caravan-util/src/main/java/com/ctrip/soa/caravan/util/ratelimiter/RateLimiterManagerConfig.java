package com.ctrip.soa.caravan.util.ratelimiter;

import com.ctrip.soa.caravan.common.value.checker.NullArgumentChecker;
import com.ctrip.soa.caravan.configuration.facade.TypedDynamicCachedCorrectedProperties;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class RateLimiterManagerConfig {

    private TypedDynamicCachedCorrectedProperties _properties;

    public RateLimiterManagerConfig(TypedDynamicCachedCorrectedProperties properties) {
        NullArgumentChecker.DEFAULT.check(properties, "properties");
        _properties = properties;
    }

    public TypedDynamicCachedCorrectedProperties properties() {
        return _properties;
    }

}
