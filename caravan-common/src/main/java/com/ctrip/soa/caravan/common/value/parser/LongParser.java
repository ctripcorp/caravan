package com.ctrip.soa.caravan.common.value.parser;

import com.ctrip.soa.caravan.common.value.StringValues;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class LongParser implements ValueParser<Long> {

    public static final LongParser DEFAULT = new LongParser();

    @Override
    public Long parse(String value) {
        if (StringValues.isNullOrWhitespace(value))
            return null;

        return Long.parseLong(value.trim());
    }

}
