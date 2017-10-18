package com.ctrip.soa.caravan.util.serializer.date;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.junit.Assert;
import org.junit.Test;

import com.ctrip.soa.caravan.common.value.DateValues;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class StandardDateTest {

    @Test
    public void standardTimeDeserialization() {
        String dateTime = "2017-04-21T10:00:00.555+08:00";
        boolean isValid = StandardDateSerializer.INSTANCE.isValid(dateTime);
        Assert.assertTrue(isValid);

        String dateTime2 = "2017-04-21T10:00:00.555-08:00";
        boolean isValid2 = StandardDateSerializer.INSTANCE.isValid(dateTime2);
        Assert.assertTrue(isValid2);

        String dateTime3 = "2017-04-21T10:00:00.555-00:00";
        boolean isValid3 = StandardDateSerializer.INSTANCE.isValid(dateTime3);
        Assert.assertTrue(isValid3);

        String dateTime4 = "2017-04-21T10:00:00.555+00:00";
        boolean isValid4 = StandardDateSerializer.INSTANCE.isValid(dateTime4);
        Assert.assertTrue(isValid4);

        String dateTime5 = "2017-04-21T10:00:00.555Z";
        boolean isValid5 = StandardDateSerializer.INSTANCE.isValid(dateTime5);
        Assert.assertTrue(isValid5);

        String dateTime6 = "2017-04-21T10:00:00.555z";
        boolean isValid6 = StandardDateSerializer.INSTANCE.isValid(dateTime6);
        Assert.assertFalse(isValid6);
    }

    @Test
    public void standardTimeDeserializationNegative() {
        String dateTime = "2017-04-21T10:00:00.555z";
        boolean isValid = StandardDateSerializer.INSTANCE.isValid(dateTime);
        Assert.assertFalse(isValid);

        String dateTime2 = "2017-04-21T10:00:00.555+8";
        boolean isValid2 = StandardDateSerializer.INSTANCE.isValid(dateTime2);
        Assert.assertFalse(isValid2);

        String dateTime3 = "3017-04-31 10:00:00.555+08:00";
        boolean isValid3 = StandardDateSerializer.INSTANCE.isValid(dateTime3);
        Assert.assertFalse(isValid3);
    }

    @Test
    public void standardTimeDeserialization2() {
        GregorianCalendar calendar = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
        calendar.set(2017, 3, 21, 2, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        System.out.println("Calendar: " + DateValues.toString(calendar));

        TimeZone timeZone = TimeZone.getTimeZone("GMT+08:00");
        TimeZone timeZone2 = TimeZone.getTimeZone("UTC");
        TimeZone timeZone3 = TimeZone.getTimeZone("GMT-08:00");

        String dateTime = "2017-04-21T10:00:00.000+08:00";
        GregorianCalendar date = StandardDateSerializer.INSTANCE.deserialize(dateTime);
        Assert.assertEquals(calendar.getTimeInMillis(), date.getTimeInMillis());
        Assert.assertEquals(timeZone, date.getTimeZone());
        Assert.assertEquals(timeZone.getRawOffset(), date.getTimeZone().getRawOffset());
        System.out.println("date: " + DateValues.toString(date));
        System.out.println("timeZone: " + date.getTimeZone());

        String dateTime2 = "2017-04-21T02:00:00.000+00:00";
        GregorianCalendar date2 = StandardDateSerializer.INSTANCE.deserialize(dateTime2);
        Assert.assertEquals(calendar.getTimeInMillis(), date2.getTimeInMillis());
        Assert.assertEquals(timeZone2, date2.getTimeZone());
        Assert.assertEquals(timeZone2.getRawOffset(), date2.getTimeZone().getRawOffset());
        System.out.println("date2: " + DateValues.toString(date2));
        System.out.println("timeZone: " + date2.getTimeZone());

        String dateTime3 = "2017-04-21T02:00:00.000-00:00";
        GregorianCalendar date3 = StandardDateSerializer.INSTANCE.deserialize(dateTime3);
        Assert.assertEquals(calendar.getTimeInMillis(), date3.getTimeInMillis());
        Assert.assertEquals(timeZone2, date3.getTimeZone());
        Assert.assertEquals(timeZone2.getRawOffset(), date3.getTimeZone().getRawOffset());
        System.out.println("date3: " + DateValues.toString(date3));
        System.out.println("timeZone: " + date3.getTimeZone());

        String dateTime4 = "2017-04-21T02:00:00.000Z";
        GregorianCalendar date4 = StandardDateSerializer.INSTANCE.deserialize(dateTime4);
        Assert.assertEquals(calendar.getTimeInMillis(), date4.getTimeInMillis());
        Assert.assertEquals(timeZone2, date4.getTimeZone());
        Assert.assertEquals(timeZone2.getRawOffset(), date4.getTimeZone().getRawOffset());
        System.out.println("date4: " + DateValues.toString(date4));
        System.out.println("timeZone: " + date4.getTimeZone());

        String dateTime5 = "2017-04-20T18:00:00.000-08:00";
        GregorianCalendar date5 = StandardDateSerializer.INSTANCE.deserialize(dateTime5);
        Assert.assertEquals(calendar.getTimeInMillis(), date5.getTimeInMillis());
        Assert.assertEquals(timeZone3, date5.getTimeZone());
        Assert.assertEquals(timeZone3.getRawOffset(), date5.getTimeZone().getRawOffset());
        System.out.println("date5: " + DateValues.toString(date5));
        System.out.println("timeZone: " + date5.getTimeZone());
    }

    @Test
    public void standardTimeSerialization() {
        TimeZone defaultTimeZone = TimeZone.getDefault();
        TimeZone timeZone = TimeZone.getTimeZone("GMT+08:00");
        TimeZone timeZone2 = TimeZone.getTimeZone("UTC");
        TimeZone timeZone3 = TimeZone.getTimeZone("GMT-08:00");
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.set(2017, 3, 21, 10, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        System.out.println("standardTimeSerialization start");

        String dateTime = "2017-04-21T10:00:00.000+08:00";
        String sDateTime = StandardDateSerializer.INSTANCE.serialize(calendar);
        Assert.assertEquals(dateTime, sDateTime);
        System.out.println(dateTime);
        System.out.println(sDateTime);

        dateTime = "2017-04-21T10:00:00.000+08:00";
        calendar.setTimeZone(defaultTimeZone);
        sDateTime = StandardDateSerializer.INSTANCE.serialize(calendar);
        Assert.assertEquals(dateTime, sDateTime);
        System.out.println(dateTime);
        System.out.println(sDateTime);

        dateTime = "2017-04-21T10:00:00.000+08:00";
        calendar.setTimeZone(timeZone);
        sDateTime = StandardDateSerializer.INSTANCE.serialize(calendar);
        Assert.assertEquals(dateTime, sDateTime);
        System.out.println(dateTime);
        System.out.println(sDateTime);

        dateTime = "2017-04-21T02:00:00.000Z";
        calendar.setTimeZone(timeZone2);
        sDateTime = StandardDateSerializer.INSTANCE.serialize(calendar);
        Assert.assertEquals(dateTime, sDateTime);
        System.out.println(dateTime);
        System.out.println(sDateTime);

        dateTime = "2017-04-20T18:00:00.000-08:00";
        calendar.setTimeZone(timeZone3);
        sDateTime = StandardDateSerializer.INSTANCE.serialize(calendar);
        Assert.assertEquals(dateTime, sDateTime);
        System.out.println(dateTime);
        System.out.println(sDateTime);
    }

    @Test
    public void standardTimeSerializationDeserialization() {
        TimeZone defaultTimeZone = TimeZone.getDefault();
        TimeZone timeZone = TimeZone.getTimeZone("GMT+08:00");
        TimeZone timeZone2 = TimeZone.getTimeZone("UTC");
        TimeZone timeZone3 = TimeZone.getTimeZone("GMT-08:00");
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.set(2017, 3, 21, 10, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        String sDateTime = StandardDateSerializer.INSTANCE.serialize(calendar);
        GregorianCalendar calendar2 = StandardDateSerializer.INSTANCE.deserialize(sDateTime);
        Assert.assertEquals(calendar.getTimeInMillis(), calendar2.getTimeInMillis());
        Assert.assertEquals(calendar.getTimeZone().getRawOffset(), calendar2.getTimeZone().getRawOffset());
        System.out.println(DateValues.toString(calendar));
        System.out.println(DateValues.toString(calendar2));

        calendar.setTimeZone(defaultTimeZone);
        sDateTime = StandardDateSerializer.INSTANCE.serialize(calendar);
        calendar2 = StandardDateSerializer.INSTANCE.deserialize(sDateTime);
        Assert.assertEquals(calendar.getTimeInMillis(), calendar2.getTimeInMillis());
        Assert.assertEquals(calendar.getTimeZone().getRawOffset(), calendar2.getTimeZone().getRawOffset());
        System.out.println(DateValues.toString(calendar));
        System.out.println(DateValues.toString(calendar2));

        calendar.setTimeZone(timeZone);
        sDateTime = StandardDateSerializer.INSTANCE.serialize(calendar);
        calendar2 = StandardDateSerializer.INSTANCE.deserialize(sDateTime);
        Assert.assertEquals(calendar.getTimeInMillis(), calendar2.getTimeInMillis());
        Assert.assertEquals(calendar.getTimeZone(), calendar2.getTimeZone());
        System.out.println(DateValues.toString(calendar));
        System.out.println(DateValues.toString(calendar2));

        calendar.setTimeZone(timeZone2);
        sDateTime = StandardDateSerializer.INSTANCE.serialize(calendar);
        calendar2 = StandardDateSerializer.INSTANCE.deserialize(sDateTime);
        Assert.assertEquals(calendar.getTimeInMillis(), calendar2.getTimeInMillis());
        Assert.assertEquals(calendar.getTimeZone(), calendar2.getTimeZone());
        System.out.println(DateValues.toString(calendar));
        System.out.println(DateValues.toString(calendar2));

        calendar.setTimeZone(timeZone3);
        sDateTime = StandardDateSerializer.INSTANCE.serialize(calendar);
        calendar2 = StandardDateSerializer.INSTANCE.deserialize(sDateTime);
        Assert.assertEquals(calendar.getTimeInMillis(), calendar2.getTimeInMillis());
        Assert.assertEquals(calendar.getTimeZone(), calendar2.getTimeZone());
        System.out.println(DateValues.toString(calendar));
        System.out.println(DateValues.toString(calendar2));
    }

}
