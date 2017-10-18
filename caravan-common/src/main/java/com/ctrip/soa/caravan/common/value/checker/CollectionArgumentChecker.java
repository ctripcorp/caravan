package com.ctrip.soa.caravan.common.value.checker;

import java.util.Collection;

import com.ctrip.soa.caravan.common.value.CollectionValues;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class CollectionArgumentChecker implements ValueChecker<Collection<?>> {

    public static final CollectionArgumentChecker DEFAULT = new CollectionArgumentChecker();

    @Override
    public void check(Collection<?> value, String valueName) {
        if (CollectionValues.isNullOrEmpty(value))
            throw new IllegalArgumentException("argument " + valueName + " is null or empty");
    }

}
