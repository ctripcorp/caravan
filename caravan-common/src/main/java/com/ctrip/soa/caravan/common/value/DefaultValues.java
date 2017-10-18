package com.ctrip.soa.caravan.common.value;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public final class DefaultValues {

    private DefaultValues() {

    }

    public static boolean isDefault(Object value) {
        if (value == null)
            return true;

        if (value instanceof Number && ((Number)value).doubleValue() == 0.0)
            return true;

        return false;
    }
}