package com.ctrip.soa.caravan.common.value.checker;

import java.util.Map;

import com.ctrip.soa.caravan.common.value.MapValues;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class MapArgumentChecker implements ValueChecker<Map<?, ?>> {

    public static final MapArgumentChecker DEFAULT = new MapArgumentChecker();

    @Override
    public void check(Map<?, ?> value, String valueName) {
        if (MapValues.isNullOrEmpty(value))
            throw new IllegalArgumentException("argument " + valueName + " is null or empty");
    }

}
