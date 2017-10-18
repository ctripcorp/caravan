package com.ctrip.soa.caravan.util.serializer.ssjson;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.xml.datatype.XMLGregorianCalendar;

import com.ctrip.soa.caravan.common.serializer.SerializationException;
import com.ctrip.soa.caravan.common.serializer.StreamSerializer;
import com.ctrip.soa.caravan.common.serializer.StringSerializer;
import com.ctrip.soa.caravan.common.value.CollectionValues;
import com.ctrip.soa.caravan.common.value.XMLValues;
import com.fasterxml.jackson.core.JsonGenerator.Feature;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class SSJsonSerializer implements StreamSerializer, StringSerializer {

    public static final SSJsonSerializer DEFAULT = new SSJsonSerializer();

    private ObjectMapper _mapper = new ObjectMapper();
    private SSJsonSerializerConfig _config;

    public SSJsonSerializer() {
        this(null);
    }

    public SSJsonSerializer(SSJsonSerializerConfig config) {
        _config = config;

        setDefaultConfigValues();
        registerCustomModule();
        configMapper();
    }

    @Override
    public String contentType() {
        return "application/json";
    }

    @Override
    public void serialize(OutputStream os, Object obj) {
        try {
            _mapper.writeValue(os, obj);
        } catch (RuntimeException | Error ex) {
            throw ex;
        } catch (Exception ex) {
            throw new SerializationException(ex);
        }
    }

    @Override
    public <T> T deserialize(InputStream is, Class<T> clazz) {
        try {
            return _mapper.readValue(is, clazz);
        } catch (RuntimeException | Error ex) {
            throw ex;
        } catch (Exception ex) {
            throw new SerializationException(ex);
        }
    }

    @Override
    public String serialize(Object obj) {
        try {
            return _mapper.writeValueAsString(obj);
        } catch (RuntimeException | Error ex) {
            throw ex;
        } catch (Exception ex) {
            throw new SerializationException(ex);
        }
    }

    @Override
    public <T> T deserialize(String s, Class<T> clazz) {
        try {
            return _mapper.readValue(s, clazz);
        } catch (RuntimeException | Error ex) {
            throw ex;
        } catch (Exception ex) {
            throw new SerializationException(ex);
        }
    }

    private void setDefaultConfigValues() {
        if (_config == null)
            _config = SSJsonSerializerConfig.createDefault();

        if (_config.getCalendarSerializer() == null)
            _config.setCalendarSerializer(SSJsonSerializerConfig.DEFAULT_CALENDAR_SERIALIZER);

        if (CollectionValues.isNullOrEmpty(_config.getCalendarDeserializers()))
            _config.setCalendarDeserializers(new ArrayList<>(SSJsonSerializerConfig.DEFAULT_CALENDAR_DESERIALIZERS));
    }

    private void registerCustomModule() {
        SimpleModule module = new SimpleModule();
        registerAbstractTypeMappings(module);
        registerSerializers(module);
        _mapper.registerModule(module);
    }

    private void configMapper() {
        _mapper.configure(Feature.AUTO_CLOSE_TARGET, false);
        _mapper.configure(Feature.IGNORE_UNKNOWN, true);
        _mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        _mapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, false);
        _mapper.configure(JsonParser.Feature.IGNORE_UNDEFINED, true);
        _mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        _mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        _mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        _mapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
        _mapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
        _mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
    }

    private void registerAbstractTypeMappings(SimpleModule module) {
        module.addAbstractTypeMapping(XMLGregorianCalendar.class, XMLValues.xmlGregorianCalendarType());
        module.addAbstractTypeMapping(Calendar.class, GregorianCalendar.class);
    }

    private void registerSerializers(SimpleModule module) {
        registerCalendarSerializer(module);
    }

    private void registerCalendarSerializer(SimpleModule module) {
        module.addSerializer(GregorianCalendar.class, new GregorianCalendarSerializer(_config.getCalendarSerializer()));
        module.addDeserializer(GregorianCalendar.class, new GregorianCalendarDeserializer(_config.getCalendarDeserializers()));
        module.addSerializer(XMLValues.xmlGregorianCalendarType(), new XMLGregorianCalendarSerializer(_config.getCalendarSerializer()));
        module.addDeserializer(XMLValues.xmlGregorianCalendarType(), new XMLGregorianCalendarDeserializer(_config.getCalendarDeserializers()));
    }

}
