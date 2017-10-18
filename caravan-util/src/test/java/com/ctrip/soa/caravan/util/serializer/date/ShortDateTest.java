package com.ctrip.soa.caravan.util.serializer.date;

import com.ctrip.soa.caravan.common.value.DateValues;
import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Created by w.jian on 2017/5/21.
 */
/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class ShortDateTest {
    
    @Test
    public void shortDateTimeDeserialization() {
        String dateTime = "2017-04-21";
        boolean isValid = ShortDateSerializer.INSTANCE.isValid(dateTime);
        Assert.assertTrue(isValid);
        
        String dateTime1 = "2017-04-21Q";
        boolean isValid6 = ShortDateSerializer.INSTANCE.isValid(dateTime1);
        Assert.assertFalse(isValid6);
    }
    
    @Test
    public void shortDateTimeDeserializationNegative() {
        String dateTime = "2017-04-21 ";
        boolean isValid = ShortDateSerializer.INSTANCE.isValid(dateTime);
        Assert.assertFalse(isValid);
        
        String dateTime2 = "2017-04-21T";
        boolean isValid2 = ShortDateSerializer.INSTANCE.isValid(dateTime2);
        Assert.assertFalse(isValid2);
        
        String dateTime3 = "3017-04 31";
        boolean isValid3 = ShortDateSerializer.INSTANCE.isValid(dateTime3);
        Assert.assertFalse(isValid3);
    }
    
    @Test
    public void shortDateTimeDeserialization2() {
        GregorianCalendar calendar = new GregorianCalendar(TimeZone.getDefault());
        calendar.set(2017, Calendar.APRIL, 21, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        System.out.println("Calendar: " + DateValues.toString(calendar));
        
        String dateTime = "2017-04-21";
        GregorianCalendar date = ShortDateSerializer.INSTANCE.deserialize(dateTime);
        Assert.assertEquals(calendar.getTimeInMillis(), date.getTimeInMillis());
        System.out.println("date: " + DateValues.toString(date));
        System.out.println("timeZone: " + date.getTimeZone());
        
        String dateTime5 = "2017-04-20";
        GregorianCalendar date5 = ShortDateSerializer.INSTANCE.deserialize(dateTime5);
        Assert.assertNotEquals(calendar.getTimeInMillis(), date5.getTimeInMillis());
        System.out.println("date1: " + DateValues.toString(date5));
        System.out.println("timeZone: " + date5.getTimeZone());
    }
    
    @Test
    public void shortDateTimeSerialization() {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.set(2017, Calendar.APRIL, 21, 10, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        
        System.out.println("shortDateTimeSerialization start");
        
        String dateTime = "2017-04-21";
        String sDateTime = ShortDateSerializer.INSTANCE.serialize(calendar);
        Assert.assertEquals(dateTime, sDateTime);
        System.out.println(dateTime);
        System.out.println(sDateTime);
        
        dateTime = "2017-04-20";
        sDateTime = ShortDateSerializer.INSTANCE.serialize(calendar);
        Assert.assertNotEquals(dateTime, sDateTime);
        System.out.println(dateTime);
        System.out.println(sDateTime);
    }
    
    @Test
    public void shortDateTimeSerializationDeserialization() {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.set(2017, Calendar.APRIL, 21, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        
        String sDateTime = ShortDateSerializer.INSTANCE.serialize(calendar);
        GregorianCalendar calendar2 = ShortDateSerializer.INSTANCE.deserialize(sDateTime);
        Assert.assertEquals(calendar.getTimeInMillis(), calendar2.getTimeInMillis());
        Assert.assertEquals(calendar.getTimeZone().getRawOffset(), calendar2.getTimeZone().getRawOffset());
        System.out.println(DateValues.toString(calendar));
        System.out.println(DateValues.toString(calendar2));
        
        sDateTime = ShortDateSerializer.INSTANCE.serialize(calendar);
        calendar2 = ShortDateSerializer.INSTANCE.deserialize(sDateTime);
        Assert.assertEquals(calendar.getTimeInMillis(), calendar2.getTimeInMillis());
        Assert.assertEquals(calendar.getTimeZone().getRawOffset(), calendar2.getTimeZone().getRawOffset());
        System.out.println(DateValues.toString(calendar));
        System.out.println(DateValues.toString(calendar2));
    }
}
