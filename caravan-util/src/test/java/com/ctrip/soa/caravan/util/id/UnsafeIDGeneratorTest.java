package com.ctrip.soa.caravan.util.id;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class UnsafeIDGeneratorTest {
    
    @Test
    public void timeBasedRandom() {
        int length = 20;
        String id = UnsafeIDGenerator.timeBasedRandom(length);
        Assert.assertEquals(id.length(), length);
        System.out.println(id);
    }

    @Test
    public void timeBasedRandomMinLength() {
        int length = 17;
        String id = UnsafeIDGenerator.timeBasedRandom(length);
        Assert.assertNotEquals(length, id.length());
        Assert.assertEquals(UnsafeIDGenerator.TIME_BASED_RANDOM_MIN_LENGTH, id.length());
        System.out.println(id);
    }

}
