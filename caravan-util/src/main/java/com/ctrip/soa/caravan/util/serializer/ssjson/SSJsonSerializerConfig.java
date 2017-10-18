package com.ctrip.soa.caravan.util.serializer.ssjson;

import java.util.ArrayList;
import java.util.List;

import com.ctrip.soa.caravan.common.serializer.DateSerializer;
import com.ctrip.soa.caravan.util.serializer.date.ShortDateSerializer;
import com.ctrip.soa.caravan.util.serializer.date.SimpleDateSerializer;
import com.ctrip.soa.caravan.util.serializer.date.StandardDateSerializer;
import com.ctrip.soa.caravan.util.serializer.date.StandardSimpleDateSerializer;
import com.ctrip.soa.caravan.util.serializer.date.StandardWithoutMillisecondDateSerializer;
import com.ctrip.soa.caravan.util.serializer.date.WcfDateSerializer;
import com.google.common.collect.ImmutableList;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class SSJsonSerializerConfig {

    public static final DateSerializer DEFAULT_CALENDAR_SERIALIZER = WcfDateSerializer.INSTANCE;
    public static final List<DateSerializer> DEFAULT_CALENDAR_DESERIALIZERS = ImmutableList.of(
            WcfDateSerializer.INSTANCE,
            SimpleDateSerializer.INSTANCE,
            StandardDateSerializer.INSTANCE,
            ShortDateSerializer.INSTANCE,
            StandardSimpleDateSerializer.INSTANCE,
            StandardWithoutMillisecondDateSerializer.INSTANCE);

    public static SSJsonSerializerConfig createDefault() {
        SSJsonSerializerConfig config = new SSJsonSerializerConfig();
        config.setCalendarSerializer(DEFAULT_CALENDAR_SERIALIZER);
        config.setCalendarDeserializers(new ArrayList<>(DEFAULT_CALENDAR_DESERIALIZERS));
        return config;
    }

    private DateSerializer calendarSerializer;
    private List<DateSerializer> calendarDeserializers;

    public DateSerializer getCalendarSerializer() {
        return calendarSerializer;
    }

    public void setCalendarSerializer(DateSerializer calendarSerializer) {
        this.calendarSerializer = calendarSerializer;
    }

    public List<DateSerializer> getCalendarDeserializers() {
        return calendarDeserializers;
    }

    public void setCalendarDeserializers(List<DateSerializer> calendarDeserializers) {
        this.calendarDeserializers = calendarDeserializers;
    }
}
