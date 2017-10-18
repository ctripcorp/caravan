package com.ctrip.soa.caravan.common.serializer;

import java.util.GregorianCalendar;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public interface DateSerializer {

    boolean isValid(String date);

    String serialize(GregorianCalendar calendar);

    GregorianCalendar deserialize(String date);

}
