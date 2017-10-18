package com.ctrip.soa.caravan.util.serializer.ssjson;

import java.io.IOException;

import javax.xml.datatype.XMLGregorianCalendar;

import com.ctrip.soa.caravan.common.serializer.DateSerializer;
import com.ctrip.soa.caravan.common.value.checker.NullArgumentChecker;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class XMLGregorianCalendarSerializer extends StdSerializer<XMLGregorianCalendar> {

    private static final long serialVersionUID = 1L;

    private DateSerializer _serializer;

    public XMLGregorianCalendarSerializer(DateSerializer serializer) {
        super(XMLGregorianCalendar.class);

        NullArgumentChecker.DEFAULT.check(serializer, "serializer");
        _serializer = serializer;
    }

    @Override
    @SuppressWarnings("all")
    public void serialize(XMLGregorianCalendar value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (value == null) {
            gen.writeNull();
            return;
        }

        gen.writeString(_serializer.serialize(value.toGregorianCalendar()));
    }

}
