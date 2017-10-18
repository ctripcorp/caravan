package com.ctrip.soa.caravan.util.ratelimiter;

import java.util.Map;

import com.ctrip.soa.caravan.common.concurrent.collect.circularbuffer.timebucket.CounterBuffer;
import com.ctrip.soa.caravan.common.value.BooleanValues;
import com.ctrip.soa.caravan.common.value.corrector.DefaultValueCorrector;
import com.ctrip.soa.caravan.common.value.corrector.MapValueCorrector;
import com.ctrip.soa.caravan.common.value.corrector.PipelineCorrector;
import com.ctrip.soa.caravan.common.value.corrector.RangeCorrector;
import com.ctrip.soa.caravan.common.value.parser.LongValueMapParser;
import com.ctrip.soa.caravan.configuration.facade.TypedDynamicCachedCorrectedProperties;
import com.ctrip.soa.caravan.configuration.typed.TypedProperty;
import com.ctrip.soa.caravan.configuration.util.PropertyKeyGenerator;

import static com.ctrip.soa.caravan.util.ratelimiter.RateLimiterConfig.*;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
class DefaultRateLimiter implements RateLimiter {

    private String _rateLimiterId;
    private TypedProperty<Boolean> _enabledProperty;
    private TypedProperty<Long> _defaultRateLimitProperty;
    private TypedProperty<Map<String, Long>> _rateLimitMapProperty;
    private CounterBuffer<String> _counterBuffer;

    public DefaultRateLimiter(String rateLimiterId, TypedDynamicCachedCorrectedProperties properties, RateLimiterConfig rateLimiterConfig) {
        _rateLimiterId = rateLimiterId;

        String propertyKey = PropertyKeyGenerator.generateKey(_rateLimiterId, ENABLED_PROPERTY_KEY);
        _enabledProperty = properties.getBooleanProperty(propertyKey, rateLimiterConfig.enabled());

        propertyKey = PropertyKeyGenerator.generateKey(_rateLimiterId, DEFAULT_RATE_LIMIT_PROPERTY_KEY);
        _defaultRateLimitProperty = properties.getLongProperty(propertyKey, rateLimiterConfig.rateLimitPropertyConfig());

        PipelineCorrector<Long> rateLimitValueCorrector = new PipelineCorrector<>(
                new DefaultValueCorrector<>(rateLimiterConfig.rateLimitPropertyConfig().defaultValue()),
                new RangeCorrector<>(rateLimiterConfig.rateLimitPropertyConfig().lowerBound(), rateLimiterConfig.rateLimitPropertyConfig().upperBound()));
        MapValueCorrector<String, Long> rateLimitMapValueCorretor = new MapValueCorrector<>(rateLimitValueCorrector);
        propertyKey = PropertyKeyGenerator.generateKey(_rateLimiterId, RATE_LIMIT_MAP_PROPERTY_KEY);
        _rateLimitMapProperty = properties.getProperty(propertyKey, LongValueMapParser.DEFAULT, rateLimitMapValueCorretor);

        _counterBuffer = new CounterBuffer<>(rateLimiterConfig.bufferConfig());
    }

    @Override
    public String rateLimiterId() {
        return _rateLimiterId;
    }

    @Override
    public boolean isRateLimited(String identity) {
        if (BooleanValues.isFalse(_enabledProperty.typedValue()))
            return false;

        _counterBuffer.incrementCount(identity);
        if (_counterBuffer.getCount(identity) <= getRateLimit(identity))
            return false;

        _counterBuffer.decrementCount(identity);
        return true;
    }

    private long getRateLimit(String identity) {
        Map<String, Long> rateLimitMap = _rateLimitMapProperty.typedValue();
        Long rateLimit = null;
        if (rateLimitMap != null)
            rateLimit = rateLimitMap.get(identity);

        if (rateLimit == null)
            rateLimit = _defaultRateLimitProperty.typedValue();

        return rateLimit == null ? Long.MAX_VALUE : rateLimit.longValue();
    }

}
