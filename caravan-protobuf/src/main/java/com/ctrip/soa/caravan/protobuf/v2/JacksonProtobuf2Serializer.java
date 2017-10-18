package com.ctrip.soa.caravan.protobuf.v2;

import com.ctrip.soa.caravan.common.serializer.BytesSerializer;
import com.ctrip.soa.caravan.common.serializer.StreamSerializer;
import com.fasterxml.jackson.core.JsonGenerator.Feature;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.InputStream;
import java.io.OutputStream;

public class JacksonProtobuf2Serializer implements StreamSerializer, BytesSerializer {

    public static final JacksonProtobuf2Serializer INSTANCE = new JacksonProtobuf2Serializer();

    private ProtobufMapperWrapper _mapperWrapper;

    private JacksonProtobuf2Serializer() {
        _mapperWrapper = new ProtobufMapperWrapper();
        _mapperWrapper.mapper().configure(Feature.AUTO_CLOSE_TARGET, false);
        _mapperWrapper.mapper().configure(Feature.IGNORE_UNKNOWN, true);
        _mapperWrapper.mapper().configure(JsonParser.Feature.IGNORE_UNDEFINED, true);
        _mapperWrapper.mapper().configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, false);
        _mapperWrapper.mapper().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        _mapperWrapper.mapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        _mapperWrapper.mapper().configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        _mapperWrapper.mapper().configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
        _mapperWrapper.mapper().configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
    }

    @Override
    public String contentType() {
        return "application/x-protobuf";
    }

    @Override
    public void serialize(OutputStream os, Object obj) {
        _mapperWrapper.writeValue(os, obj);
    }

    @Override
    public <T> T deserialize(InputStream is, Class<T> clazz) {
        return _mapperWrapper.readValue(is, clazz);
    }

    @Override
    public byte[] serialize(Object obj) {
        return _mapperWrapper.writeValue(obj);
    }

    @Override
    public <T> T deserialize(byte[] is, Class<T> clazz) {
        return _mapperWrapper.readValue(is, clazz);
    }

}
