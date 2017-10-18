package com.ctrip.soa.caravan.common.value;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public final class ArrayValues {

    public static final ArrayValues DEFAULT = new ArrayValues();

    private ArrayValues() {

    }

    public static <T> boolean isNullOrEmpty(T[] array) {
        return array == null || array.length == 0;
    }

    public static <T> void checkNullOrEmpty(T[] value, String valueName) {
        if (isNullOrEmpty(value))
            throw new IllegalArgumentException("argument " + valueName + " is null or empty");
    }

    public static <T> List<T> asList(T[] array) {
        if (array == null)
            return null;

        return Arrays.asList(array);
    }

}
