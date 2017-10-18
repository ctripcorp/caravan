package com.ctrip.soa.caravan.util.serializer.date;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class StandardWithoutMillisecondDataTest {
    
    @Test
    public void StandardWithoutMillisecondTest0() {
        String dateTime = "2017-04-21Q00:00:00+08:00";
        Assert.assertFalse(StandardWithoutMillisecondDateSerializer.INSTANCE.isValid(dateTime));
    }
    
    @Test
    public void StandardWithoutMillisecondTest1() {
        String dateTime = "2017-04-01T00:00:00+08:00";
        Assert.assertTrue(StandardWithoutMillisecondDateSerializer.INSTANCE.isValid(dateTime));
    }
    
    @Test
    public void StandardWithoutMillisecondTest2() {
        String dateTime = "2017-04-21T00:00:00Z";
        Assert.assertTrue(StandardWithoutMillisecondDateSerializer.INSTANCE.isValid(dateTime));
    }
    
    @Test
    public void StandardWithoutMillisecondTest3() {
        String dateTime = "2017-04-21T00:00:00";
        Assert.assertTrue(StandardWithoutMillisecondDateSerializer.INSTANCE.isValid(dateTime));
    }
}
