package com.ctrip.soa.caravan.util.serializer.date;

import java.util.GregorianCalendar;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class WcfDateTest {

    @Test
    public void utcTimeDeserialization() {
        String dateTime = "/Date(1489398889181+0800)/";
        boolean isValid = WcfDateSerializer.INSTANCE.isValid(dateTime);
        Assert.assertTrue(isValid);

        String dateTime2 = "/Date(1489398889181)/";
        boolean isValid2 = WcfDateSerializer.INSTANCE.isValid(dateTime2);
        Assert.assertTrue(isValid2);

        String dateTime3 = "/Date(-1489398889181)/";
        boolean isValid3 = WcfDateSerializer.INSTANCE.isValid(dateTime3);
        Assert.assertTrue(isValid3);

        String dateTime4 = "/Date(+1489398889181+0800)/";
        boolean isValid4 = WcfDateSerializer.INSTANCE.isValid(dateTime4);
        Assert.assertTrue(isValid4);

        String dateTime5 = "/Date(-1489398889181+0800)/";
        boolean isValid5 = WcfDateSerializer.INSTANCE.isValid(dateTime5);
        Assert.assertTrue(isValid5);

        String dateTime6 = "/Date(+1489398889181)/";
        boolean isValid6 = WcfDateSerializer.INSTANCE.isValid(dateTime6);
        Assert.assertTrue(isValid6);
    }
    
    @Test
    public void utcTimeDeserializationNegative() {
        String dateTime = "/Date(--1489398889181+0800)/";
        boolean isValid = WcfDateSerializer.INSTANCE.isValid(dateTime);
        Assert.assertFalse(isValid);

        String dateTime2 = "/Date(++1489398889181)/";
        boolean isValid2 = WcfDateSerializer.INSTANCE.isValid(dateTime2);
        Assert.assertFalse(isValid2);

        String dateTime3 = "/Date(1489398889181-)/";
        boolean isValid3 = WcfDateSerializer.INSTANCE.isValid(dateTime3);
        Assert.assertFalse(isValid3);

        String dateTime4 = "/Date(+1489398889181 0800)/";
        boolean isValid4 = WcfDateSerializer.INSTANCE.isValid(dateTime4);
        Assert.assertFalse(isValid4);

        String dateTime5 = "/Date(-1489398889181_0800)/";
        boolean isValid5 = WcfDateSerializer.INSTANCE.isValid(dateTime5);
        Assert.assertFalse(isValid5);

        String dateTime6 = "/Date(+1489398889181++)/";
        boolean isValid6 = WcfDateSerializer.INSTANCE.isValid(dateTime6);
        Assert.assertFalse(isValid6);
    }
    
    @Test
    public void utcTimeDeserialization2() {
        long ms = 1489398889181l;
        String dateTime = "/Date(1489398889181+0800)/";
        GregorianCalendar date = WcfDateSerializer.INSTANCE.deserialize(dateTime);
        Assert.assertEquals(ms, date.getTimeInMillis());

        String dateTime2 = "/Date(1489398889181)/";
        GregorianCalendar date2 = WcfDateSerializer.INSTANCE.deserialize(dateTime2);
        Assert.assertEquals(ms, date2.getTimeInMillis());

        String dateTime3 = "/Date(-1489398889181)/";
        GregorianCalendar date3 = WcfDateSerializer.INSTANCE.deserialize(dateTime3);
        Assert.assertEquals(-ms, date3.getTimeInMillis());

        String dateTime4 = "/Date(+1489398889181+0800)/";
        GregorianCalendar date4 = WcfDateSerializer.INSTANCE.deserialize(dateTime4);
        Assert.assertEquals(ms, date4.getTimeInMillis());

        String dateTime5 = "/Date(-1489398889181+0800)/";
        GregorianCalendar date5 = WcfDateSerializer.INSTANCE.deserialize(dateTime5);
        Assert.assertEquals(-ms, date5.getTimeInMillis());

        String dateTime6 = "/Date(+1489398889181)/";
        GregorianCalendar date6 = WcfDateSerializer.INSTANCE.deserialize(dateTime6);
        Assert.assertEquals(ms, date6.getTimeInMillis());
    }
 

}
