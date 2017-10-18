package com.ctrip.soa.caravan.util.serializer.filterable;

import java.io.InputStream;
import java.io.OutputStream;

import com.ctrip.soa.caravan.common.serializer.SerializationException;
import com.ctrip.soa.caravan.common.serializer.StreamSerializer;
import com.ctrip.soa.caravan.common.serializer.StringSerializer;
import com.ctrip.soa.caravan.common.value.checker.NullArgumentChecker;
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
public class FilterableJsonSerializer implements StreamSerializer, StringSerializer {

    private volatile ObjectMapper _mapper = new ObjectMapper();

    public FilterableJsonSerializer(FilterableJsonSerializerConfig config) {
        NullArgumentChecker.DEFAULT.check(config, "config");

        ObjectMapper mapper = new ObjectMapper();

        SimpleModule module = new SimpleModule();
        module.setSerializerModifier(new FilterableBeanSerializerModifier(config));
        mapper.registerModule(module);

        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        mapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        mapper.configure(Feature.AUTO_CLOSE_TARGET, false);
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);

        _mapper = mapper;
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

}
