package com.ctrip.soa.caravan.common.value;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public final class NullValues {

    private NullValues() {

    }

    public static <V> V NULL() {
        return null;
    }

}
