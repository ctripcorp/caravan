package com.ctrip.soa.caravan.common.value;

import java.util.Collection;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public final class CollectionValues {

    public static boolean isNullOrEmpty(Collection<?> value) {
        return value == null || value.isEmpty();
    }

    private CollectionValues() {

    }

}
