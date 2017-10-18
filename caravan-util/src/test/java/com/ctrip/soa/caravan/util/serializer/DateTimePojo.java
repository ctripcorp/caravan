package com.ctrip.soa.caravan.util.serializer;

import java.util.GregorianCalendar;

import javax.xml.datatype.XMLGregorianCalendar;

import com.ctrip.soa.caravan.common.value.XMLValues;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class DateTimePojo {

    public static final DateTimePojo SAMPLE;

    static {
        SAMPLE = new DateTimePojo();
        // 2017-02-14 00:00:00, month is 0 based
        GregorianCalendar calendar = new GregorianCalendar(2017, 1, 14, 0, 0, 0);
        SAMPLE.setTime(XMLValues.toXMLGregorianCalendar(calendar));
    }

    private XMLGregorianCalendar time;

    public XMLGregorianCalendar getTime() {
        return time;
    }

    public void setTime(XMLGregorianCalendar time) {
        this.time = time;
    }

}
