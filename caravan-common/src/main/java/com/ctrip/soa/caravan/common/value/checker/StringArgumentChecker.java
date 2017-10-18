package com.ctrip.soa.caravan.common.value.checker;

import com.ctrip.soa.caravan.common.value.StringValues;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class StringArgumentChecker implements ValueChecker<String> {

    public static final StringArgumentChecker DEFAULT = new StringArgumentChecker();

    @Override
    public void check(String value, String valueName) {
        if (StringValues.isNullOrWhitespace(value))
            throw new IllegalArgumentException("argument " + valueName + " is null or whitespace");
    }

}
