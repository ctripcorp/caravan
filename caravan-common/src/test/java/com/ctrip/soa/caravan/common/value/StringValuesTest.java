package com.ctrip.soa.caravan.common.value;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class StringValuesTest {

    @Test
    public void toStringTest() {
        Object obj = null;
        String value = String.valueOf(obj);
        Assert.assertFalse(value == null);

        value = StringValues.toString(obj);
        Assert.assertTrue(value == null);
    }

}
