package com.ctrip.soa.caravan.common.value;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public final class BooleanValues {

    public static boolean isTrue(Boolean value) {
        return Boolean.TRUE.equals(value);
    }

    public static boolean isFalse(Boolean value) {
        return Boolean.FALSE.equals(value);
    }

    private BooleanValues() {

    }

}
