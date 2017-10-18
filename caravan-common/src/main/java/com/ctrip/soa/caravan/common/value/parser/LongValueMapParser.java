package com.ctrip.soa.caravan.common.value.parser;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ctrip.soa.caravan.common.value.converter.MapValueConverter;
import com.ctrip.soa.caravan.common.value.converter.ValueConverter;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class LongValueMapParser implements ValueParser<Map<String, Long>> {

    public static final LongValueMapParser DEFAULT = new LongValueMapParser();

    private static final Logger _logger = LoggerFactory.getLogger(LongValueMapParser.class);

    private static final MapValueConverter<String, String, Long> _mapValueConverter = new MapValueConverter<>(
            new ValueConverter<String, Long>() {
                @Override
                public Long convert(String source) {
                    try {
                        return Long.parseLong(source);
                    } catch (Exception ex) {
                        _logger.warn("parse long value failed. long value: {}", source);
                        return null;
                    }
                }
            });

    @Override
    public Map<String, Long> parse(String value) {
        Map<String, String> mapValue = MapParser.DEFAULT.parse(value);
        return _mapValueConverter.convert(mapValue);
    }

}
