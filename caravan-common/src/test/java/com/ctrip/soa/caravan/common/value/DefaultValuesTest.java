package com.ctrip.soa.caravan.common.value;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class DefaultValuesTest {

    @Test
    public void isDefaultTest() {
        Assert.assertTrue(DefaultValues.isDefault(0));
        Assert.assertTrue(DefaultValues.isDefault(0.0));
        Assert.assertTrue(DefaultValues.isDefault(null));

        Assert.assertFalse(DefaultValues.isDefault(0.1));
        Assert.assertFalse(DefaultValues.isDefault(1));
        Assert.assertFalse(DefaultValues.isDefault(true));
        Assert.assertFalse(DefaultValues.isDefault(false));
    }

}
