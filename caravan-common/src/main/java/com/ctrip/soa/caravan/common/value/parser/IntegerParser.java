package com.ctrip.soa.caravan.common.value.parser;

import com.ctrip.soa.caravan.common.value.StringValues;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class IntegerParser implements ValueParser<Integer> {

    public static final IntegerParser DEFAULT = new IntegerParser();

    @Override
    public Integer parse(String value) {
        if (StringValues.isNullOrWhitespace(value))
            return null;

        return Integer.parseInt(value.trim());
    }

}
