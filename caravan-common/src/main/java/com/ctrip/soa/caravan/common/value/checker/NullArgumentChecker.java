package com.ctrip.soa.caravan.common.value.checker;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class NullArgumentChecker implements ValueChecker<Object> {
    
    public static final NullArgumentChecker DEFAULT = new NullArgumentChecker();

    @Override
    public void check(Object value, String valueName) {
        if (value == null)
            throw new IllegalArgumentException("argument " + valueName + " is null");
    }

}
