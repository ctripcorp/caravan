package com.ctrip.soa.caravan.common.value.checker;

import java.util.Collection;
import java.util.Map;

import com.ctrip.soa.caravan.common.value.ArrayValues;

/**
 * Created by fang_j on 2017/3/1.
 */
public class ValueCheckers {

    public static void notNull(Object value, String valueName) {
        NullArgumentChecker.DEFAULT.check(value, valueName);
    }

    public static void notNullOrWhiteSpace(String value, String valueName) {
        StringArgumentChecker.DEFAULT.check(value, valueName);
    }

    public static void notNullOrEmpty(Map<?, ?> value, String valueName) {
        MapArgumentChecker.DEFAULT.check(value, valueName);
    }

    public static void notNullOrEmpty(Collection<?> value, String valueName) {
        CollectionArgumentChecker.DEFAULT.check(value, valueName);
    }

    public static <T> void notNullOrEmpty(T[] value, String valueName) {
        if (ArrayValues.isNullOrEmpty(value))
            throw new IllegalArgumentException("argument " + valueName + " is null or empty");
    }

}
