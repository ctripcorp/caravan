package com.ctrip.soa.caravan.ribbon.server;

import com.ctrip.soa.caravan.common.concurrent.collect.circularbuffer.timebucket.TimeBufferConfig;
import com.ctrip.soa.caravan.common.value.corrector.ValueCorrector;
import com.ctrip.soa.caravan.configuration.util.RangePropertyConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by w.jian on 2016/8/4.
 */
class TimeBufferConfigCorrector implements ValueCorrector<TimeBufferConfig> {

    private static Logger _logger = LoggerFactory.getLogger(TimeBufferConfigCorrector.class);

    private RangePropertyConfig<Long> _bufferTimeWindowRange;
    private RangePropertyConfig<Long> _bucketTimeWindowRange;

    TimeBufferConfig DefaultValue;

    public TimeBufferConfigCorrector(RangePropertyConfig<Long> bufferTimeWindow, RangePropertyConfig<Long> bucketTimeWindow) {
        _bufferTimeWindowRange = bufferTimeWindow;
        _bucketTimeWindowRange = bucketTimeWindow;

        DefaultValue = new TimeBufferConfig(bufferTimeWindow.defaultValue(), bucketTimeWindow.defaultValue());
    }

    @Override
    public TimeBufferConfig correct(TimeBufferConfig value) {
        if(value == null)
            return null;

        long bufferTimeWindow = value.bufferTimeWindow();
        long bucketTimeWindow = value.bucketTimeWindow();

        if(bufferTimeWindow < _bufferTimeWindowRange.lowerBound())
            bufferTimeWindow = _bufferTimeWindowRange.lowerBound();
        if(bufferTimeWindow > _bufferTimeWindowRange.upperBound())
            bufferTimeWindow = _bufferTimeWindowRange.upperBound();

        if(bucketTimeWindow < _bucketTimeWindowRange.lowerBound())
            bucketTimeWindow = _bucketTimeWindowRange.lowerBound();
        if(bucketTimeWindow > _bucketTimeWindowRange.upperBound())
            bucketTimeWindow = _bucketTimeWindowRange.upperBound();

        if(bufferTimeWindow == value.bufferTimeWindow() && bucketTimeWindow == value.bucketTimeWindow())
            return value;

        if(bufferTimeWindow % bucketTimeWindow != 0) {
            _logger.warn(String.format("Buffer time window %s cannot be divided by bucket time window %s.",
                    bufferTimeWindow, bucketTimeWindow));
            return null;
        }

        return new TimeBufferConfig(bufferTimeWindow, bucketTimeWindow);
    }
}
