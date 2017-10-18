package com.ctrip.soa.caravan.util.serializer.ssjson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.xml.datatype.XMLGregorianCalendar;

import org.junit.Assert;
import org.junit.Test;

import com.ctrip.soa.caravan.common.value.DateValues;
import com.ctrip.soa.caravan.common.value.XMLValues;
import com.ctrip.soa.caravan.util.serializer.Color;
import com.ctrip.soa.caravan.util.serializer.ComplexPojo;
import com.ctrip.soa.caravan.util.serializer.DateTimePojo;
import com.ctrip.soa.caravan.util.serializer.ObjectFieldPojo;
import com.ctrip.soa.caravan.util.serializer.SimpleEnumPojo;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class SSJsonSerializerTest {

    @Test
    public void serializePojo01() throws Exception {
        ComplexPojo obj = ComplexPojo.SAMPLE;
        ComplexPojo obj2 = null;
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            SSJsonSerializer.DEFAULT.serialize(os, obj);
            try (ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray())) {
                obj2 = SSJsonSerializer.DEFAULT.deserialize(is, ComplexPojo.class);
            }
        }

        Assert.assertNotNull(obj2);
        Assert.assertNotEquals(obj, obj2);
    }

    @Test
    public void serializePojo02() throws Exception {
        ComplexPojo obj = ComplexPojo.SAMPLE;
        ComplexPojo obj2 = null;
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            SSJsonSerializer.DEFAULT.serialize(os, obj);
            try (ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray())) {
                obj2 = SSJsonSerializer.DEFAULT.deserialize(is, ComplexPojo.class);
            }
        }

        Assert.assertNotNull(obj2);
        Assert.assertNotEquals(obj, obj2);

        String s1 = SSJsonSerializer.DEFAULT.serialize(obj);
        String s2 = SSJsonSerializer.DEFAULT.serialize(obj2);
        Assert.assertEquals(s1, s2);
    }

    @Test
    public void serializeDateTimePojo() throws Exception {
        DateTimePojo obj = DateTimePojo.SAMPLE;
        DateTimePojo obj2 = null;
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            SSJsonSerializer.DEFAULT.serialize(os, obj);
            try (ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray())) {
                obj2 = SSJsonSerializer.DEFAULT.deserialize(is, DateTimePojo.class);
            }
        }

        Assert.assertNotNull(obj2);
        Assert.assertNotEquals(obj, obj2);

        String s1 = SSJsonSerializer.DEFAULT.serialize(obj);
        String s2 = SSJsonSerializer.DEFAULT.serialize(obj2);
        Assert.assertEquals(s1, s2);
    }

    @Test
    public void timeZoneTest() {
        TimeZone defaultTimeZone = TimeZone.getDefault();
        TimeZone defaultTimeZone2 = DateValues.toGregorianCalendar(new Date()).getTimeZone();
        Assert.assertEquals(defaultTimeZone, defaultTimeZone2);
    }

    @Test
    public void calendarValueTest() {
        GregorianCalendar calendar = new GregorianCalendar(2017, 1, 14, 0, 0, 0);
        XMLGregorianCalendar xmlGregorianCalendar = XMLValues.toXMLGregorianCalendar(calendar);
        GregorianCalendar calendar2 = XMLValues.toGregorianCalendar(xmlGregorianCalendar);
        Assert.assertEquals(calendar.getTimeInMillis(), calendar2.getTimeInMillis());
    }

    @Test
    public void serializeEnum() throws IOException {
        SimpleEnumPojo enumPojo = new SimpleEnumPojo();
        enumPojo.setColor(Color.RED);
        try (ByteArrayOutputStream os = serialize(enumPojo)) {
            SimpleEnumPojo result = deserialize(os, SimpleEnumPojo.class);
            Assert.assertEquals(result.getColor().value(), enumPojo.getColor().value());
        }
    }

    @Test
    public void objectFieldTest() {
    	String dotNetSerializedJson = "{\"Item\":{\"__type\":\"TestServices.CityIDType\",\"cityID\":90043},\"OrderName\":\"orderNameTest\",\"HotelType\":3}";

    	String serializedJson = SSJsonSerializer.DEFAULT.serialize(ObjectFieldPojo.INSTANCE);
		assertEquals(dotNetSerializedJson, serializedJson);

		ObjectFieldPojo deserializedObj = SSJsonSerializer.DEFAULT.deserialize(serializedJson, ObjectFieldPojo.class);
		assertEquals(ObjectFieldPojo.INSTANCE.getOrderName(), deserializedObj.getOrderName());
    	assertEquals(ObjectFieldPojo.INSTANCE.getHotelType(), deserializedObj.getHotelType());
    	assertEquals(ObjectFieldPojo.INSTANCE.getItem(), deserializedObj.getItem());
    }

    @Test
    public void specificValueTest() {
        String specificJson = "{\"value\": \"abcd\1nefg\"}";
        TestEntity testEntity = SSJsonSerializer.DEFAULT.deserialize(new ByteArrayInputStream(specificJson.getBytes()), TestEntity.class);
        assertNotNull(testEntity.getValue());
    }

    private ByteArrayOutputStream serialize(Object value) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        SSJsonSerializer.DEFAULT.serialize(os, value);
        return os;
    }

    private <T> T deserialize(ByteArrayOutputStream os, Class<T> clazz) throws IOException {
        try (ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray())) {
            return SSJsonSerializer.DEFAULT.deserialize(is, clazz);
        }
    }
}
