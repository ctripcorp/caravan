package com.ctrip.soa.caravan.ribbon.server;

import com.ctrip.soa.caravan.common.concurrent.collect.circularbuffer.timebucket.TimeBufferConfig;
import com.ctrip.soa.caravan.common.value.parser.MapParser;
import com.ctrip.soa.caravan.common.value.parser.ValueParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created by w.jian on 2016/8/4.
 */
class TimeBufferConfigParser implements ValueParser<TimeBufferConfig> {

    private static Logger _logger = LoggerFactory.getLogger(TimeBufferConfigParser.class);

    static final String BufferTimeWindowConfigKey = "buffer-time-window";
    static final String BucketTimeWindowConfigKey = "bucket-time-window";

    @Override
    public TimeBufferConfig parse(String value) {
        try {
            Map<String, String> configMap = MapParser.DEFAULT.parse(value);
            if (configMap == null)
                return null;

            int bufferTimeWindow = Integer.parseInt(configMap.get(BufferTimeWindowConfigKey));
            int bucketTimeWindow = Integer.parseInt(configMap.get(BucketTimeWindowConfigKey));
            return new TimeBufferConfig(bufferTimeWindow, bucketTimeWindow);
        } catch (Throwable t) {
            String message = String.format("Fail to parse time buffer config! Value: %s.", value);
            _logger.warn(message, t);
            return null;
        }
    }
}
