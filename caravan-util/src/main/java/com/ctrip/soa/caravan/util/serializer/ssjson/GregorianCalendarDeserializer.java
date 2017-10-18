package com.ctrip.soa.caravan.util.serializer.ssjson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import com.ctrip.soa.caravan.common.serializer.DateSerializer;
import com.ctrip.soa.caravan.common.value.StringValues;
import com.ctrip.soa.caravan.common.value.checker.CollectionArgumentChecker;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class GregorianCalendarDeserializer extends StdDeserializer<GregorianCalendar> {

    private static final long serialVersionUID = 1L;

    private List<DateSerializer> _serializers = new ArrayList<>();

    public GregorianCalendarDeserializer(List<DateSerializer> serializers) {
        super(Calendar.class);

        CollectionArgumentChecker.DEFAULT.check(serializers, "serializers");
        _serializers.addAll(serializers);
    }

    @Override
    public GregorianCalendar deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String date = p.readValueAs(String.class);
        if (StringValues.isNullOrWhitespace(date))
            return null;

        for (DateSerializer serializer : _serializers) {
            if (serializer.isValid(date))
                return serializer.deserialize(date);
        }
        
        throw new JsonParseException(p, "date value cannot be deserialized: " + date);
    }

}
