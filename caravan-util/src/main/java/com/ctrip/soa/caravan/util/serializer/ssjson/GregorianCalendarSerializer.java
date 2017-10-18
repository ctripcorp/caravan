package com.ctrip.soa.caravan.util.serializer.ssjson;

import java.io.IOException;
import java.util.GregorianCalendar;

import com.ctrip.soa.caravan.common.serializer.DateSerializer;
import com.ctrip.soa.caravan.common.value.DateValues;
import com.ctrip.soa.caravan.common.value.checker.NullArgumentChecker;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class GregorianCalendarSerializer extends StdSerializer<GregorianCalendar> {

    private static final long serialVersionUID = 1L;

    private DateSerializer _serializer;

    public GregorianCalendarSerializer(DateSerializer serializer) {
        super(GregorianCalendar.class);

        NullArgumentChecker.DEFAULT.check(serializer, "serializer");
        _serializer = serializer;
    }

    @Override
    @SuppressWarnings("all")
    public void serialize(GregorianCalendar value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (value == null) {
            gen.writeNull();
            return;
        }

        gen.writeString(_serializer.serialize(DateValues.toGregorianCalendar(value.getTime())));
    }
}
