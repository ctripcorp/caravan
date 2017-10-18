package com.ctrip.soa.caravan.common.value.parser;

import com.ctrip.soa.caravan.common.value.StringValues;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class BooleanParser implements ValueParser<Boolean> {
    
    public static final BooleanParser DEFAULT = new BooleanParser();

    @Override
    public Boolean parse(String value) {
        if (StringValues.isNullOrWhitespace(value))
            return null;

        return Boolean.parseBoolean(value.trim());
    }

}
